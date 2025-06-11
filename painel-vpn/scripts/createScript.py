#!/usr/bin/python3
# scripVPn.py

import sys
import os
import subprocess
import zipfile
import socket
from pathlib import Path

def get_ip_address():
    try:
        # Tenta obter o IP da máquina de forma mais genérica
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except:
        return "localhost"

# Configurações
nome_arquivo = sys.argv[1]
password = "123456"

# Diretórios base
home_dir = str(Path.home())
base_dir = os.path.join(home_dir, ".vpn-panel")
easy_rsa_dir = os.path.join(base_dir, "easy-rsa")
pki_dir = os.path.join(easy_rsa_dir, "pki")
output_dir = os.path.join(base_dir, "certificados", nome_arquivo)
cert_zip_dir = os.path.join(base_dir, "zips")

# Criar diretórios necessários
os.makedirs(base_dir, exist_ok=True)
os.makedirs(easy_rsa_dir, exist_ok=True)
os.makedirs(output_dir, exist_ok=True)
os.makedirs(cert_zip_dir, exist_ok=True)

# Inicializar EasyRSA se necessário
if not os.path.exists(pki_dir):
    subprocess.run(
        ["easyrsa", "init-pki"],
        cwd=easy_rsa_dir,
        check=True
    )
    
    # Gerar CA se não existir
    if not os.path.exists(os.path.join(pki_dir, "ca.crt")):
        env = os.environ.copy()
        env["EASYRSA_BATCH"] = "1"
        subprocess.run(
            ["easyrsa", "build-ca", "nopass"],
            cwd=easy_rsa_dir,
            env=env,
            check=True
        )
    
    # Gerar DH se não existir
    if not os.path.exists(os.path.join(pki_dir, "dh.pem")):
        subprocess.run(
            ["easyrsa", "gen-dh"],
            cwd=easy_rsa_dir,
            check=True
        )

# Gerar requisição de certificado
env = os.environ.copy()
env["EASYRSA_BATCH"] = "1"
subprocess.run(
    ["easyrsa", "gen-req", nome_arquivo, "nopass"],
    cwd=easy_rsa_dir,
    env=env,
    check=True
)

# Assinar certificado
env["EASYRSA_PASSIN"] = f"pass:{password}"
subprocess.run(
    ["easyrsa", "sign-req", "client", nome_arquivo],
    cwd=easy_rsa_dir,
    env=env,
    check=True
)

# Copiar arquivos necessários
files_to_copy = {
    os.path.join(pki_dir, "ca.crt"): "ca.crt",
    os.path.join(pki_dir, "issued", f"{nome_arquivo}.crt"): f"{nome_arquivo}.crt",
    os.path.join(pki_dir, "private", f"{nome_arquivo}.key"): f"{nome_arquivo}.key",
    os.path.join(pki_dir, "dh.pem"): "dh.pem"
}

for source, dest in files_to_copy.items():
    if os.path.exists(source):
        dest_path = os.path.join(output_dir, dest)
        with open(source, 'rb') as src_file, open(dest_path, 'wb') as dst_file:
            dst_file.write(src_file.read())

# Criar arquivo de configuração OpenVPN
ip_servidor = get_ip_address()
ovpn_conteudo = f"""client
dev tun
proto udp
remote {ip_servidor} 1194
resolv-retry infinite
nobind
persist-key
persist-tun
ca ca.crt
cert {nome_arquivo}.crt
key {nome_arquivo}.key
remote-cert-tls server
cipher AES-256-CBC
auth SHA256
verb 3
"""

with open(os.path.join(output_dir, f"{nome_arquivo}.ovpn"), "w") as f:
    f.write(ovpn_conteudo)

# Criar arquivo ZIP
zip_path = os.path.join(cert_zip_dir, f"{nome_arquivo}.zip")
with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
    for root, _, files in os.walk(output_dir):
        for file in files:
            file_path = os.path.join(root, file)
            arcname = os.path.relpath(file_path, output_dir)
            zipf.write(file_path, arcname)

print(f"Certificado gerado com sucesso: {zip_path}")