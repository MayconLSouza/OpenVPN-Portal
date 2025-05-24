#!/usr/bin/python3
# openvpn_client_setup.py - Automated OpenVPN client certificate generator

import sys
import os
import subprocess
import zipfile
from pathlib import Path

def get_server_ip(interface='enp0s3'):
    """Get server IP address from specified network interface"""
    try:
        result = subprocess.run(
            ["ip", "-4", "addr", "show", interface],
            capture_output=True,
            text=True,
            check=True
        )
        for line in result.stdout.splitlines():
            line = line.strip()
            if line.startswith("inet "):
                return line.split()[1].split("/")[0]
    except subprocess.CalledProcessError as e:
        raise RuntimeError(f"Failed to get IP from interface {interface}: {e.stderr}") from e
    return ""

def run_easyrsa_command(args, password=None, cwd="/usr/share/easy-rsa/"):
    """Run easyrsa command with optional password handling"""
    env = os.environ.copy()
    if password:
        env["EASYRSA_PASSIN"] = f"pass:{password}"
    
    try:
        subprocess.run(
            ["./easyrsa", "--batch", *args],
            check=True,
            cwd=cwd,
            env=env
        )
    except subprocess.CalledProcessError as e:
        raise RuntimeError(f"EasyRSA command failed: {' '.join(args)} - {e.stderr}") from e

def copy_files(src_files, dest_dir):
    """Copy multiple files to destination directory"""
    dest_dir = Path(dest_dir)
    dest_dir.mkdir(parents=True, exist_ok=True)
    
    for src in src_files:
        try:
            subprocess.run(["cp", str(src), str(dest_dir)], check=True)
        except subprocess.CalledProcessError as e:
            raise RuntimeError(f"Failed to copy {src} to {dest_dir}: {e.stderr}") from e

def create_ovpn_config(client_name, server_ip, config_dir):
    """Create client .ovpn configuration file"""
    config_content = f"""client
dev tun
proto udp
remote {server_ip}
ca ca.crt
cert {client_name}.crt
key {client_name}.key
tls-client
resolv-retry infinite
nobind
persist-key
persist-tun
"""
    config_path = config_dir / f"{client_name}.ovpn"
    try:
        with open(config_path, 'w') as f:
            f.write(config_content)
    except IOError as e:
        raise RuntimeError(f"Failed to write OVPN config: {e}") from e

def create_client_zip(client_name, source_dir, dest_path):
    """Create zip archive of client files"""
    try:
        with zipfile.ZipFile(dest_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
            for root, dirs, files in os.walk(source_dir):
                for file in files:
                    file_path = Path(root) / file
                    zipf.write(file_path, file_path.relative_to(source_dir))
    except (zipfile.BadZipFile, IOError) as e:
        raise RuntimeError(f"Failed to create zip archive: {e}") from e

def main():
    if len(sys.argv) < 3:
        print(f"Usage: {sys.argv[0]} <client_name> <ca_password> [output_user]")
        sys.exit(1)
    
    client_name = sys.argv[1]
    ca_password = sys.argv[2]
    output_user = sys.argv[3] if len(sys.argv) > 3 else "usuario"
    
    try:
        # Set up paths
        easyrsa_pki = Path("/usr/share/easy-rsa/pki")
        client_dir = Path(f"/etc/openvpn/client/{client_name}")
        user_dir = Path(f"/home/{output_user}/{client_name}")
        zip_path = Path(f"/home/{output_user}/{client_name}.zip")
        
        # Get server IP
        server_ip = get_server_ip()
        if not server_ip:
            print("Warning: Could not determine server IP, using empty value")
        
        # Generate client certificate
        run_easyrsa_command(["gen-req", client_name, "nopass"])
        run_easyrsa_command(["sign-req", "client", client_name], ca_password)
        
        # Create client directory and copy files
        client_files = [
            easyrsa_pki / "ca.crt",
            easyrsa_pki / "issued" / f"{client_name}.crt",
            easyrsa_pki / "private" / f"{client_name}.key",
            easyrsa_pki / "dh.pem"
        ]
        copy_files(client_files, client_dir)
        
        # Create OVPN config file
        create_ovpn_config(client_name, server_ip, client_dir)
        
        # Copy to user directory and set permissions
        subprocess.run(["cp", "-r", str(client_dir), str(user_dir.parent)], check=True)
        subprocess.run(["chown", "-R", f"{output_user}:{output_user}", str(user_dir)], check=True)
        
        # Create zip archive
        create_client_zip(client_name, user_dir, zip_path)
        subprocess.run(["chown", f"{output_user}:{output_user}", str(zip_path)], check=True)
        
        print(f"Successfully created client configuration for {client_name}")
        print(f"Files are available in {user_dir} and {zip_path}")
        
    except Exception as e:
        print(f"Error: {str(e)}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()