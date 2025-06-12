#!/usr/bin/python3

import sys
import os
import shutil
from pathlib import Path

def delete_certificate(cert_id):
    try:
        # Diretórios base
        home_dir = str(Path.home())
        base_dir = os.path.join(home_dir, ".vpn-panel")
        easy_rsa_dir = os.path.join(base_dir, "easy-rsa")
        output_dir = os.path.join(base_dir, "certificados", cert_id)
        cert_zip = os.path.join(base_dir, "zips", f"{cert_id}.zip")
        
        # Remover arquivos do EasyRSA
        files_to_remove = [
            os.path.join(easy_rsa_dir, "pki", "issued", f"{cert_id}.crt"),
            os.path.join(easy_rsa_dir, "pki", "private", f"{cert_id}.key"),
            os.path.join(easy_rsa_dir, "pki", "reqs", f"{cert_id}.req")
        ]
        
        # Remover arquivos
        for file_path in files_to_remove:
            if os.path.exists(file_path):
                os.remove(file_path)
                print(f"Arquivo removido: {file_path}")
        
        # Remover diretório de certificados
        if os.path.exists(output_dir):
            shutil.rmtree(output_dir)
            print(f"Diretório removido: {output_dir}")
        
        # Remover arquivo ZIP
        if os.path.exists(cert_zip):
            os.remove(cert_zip)
            print(f"Arquivo ZIP removido: {cert_zip}")
        
        # Revogar certificado no EasyRSA
        try:
            env = os.environ.copy()
            env["EASYRSA_BATCH"] = "1"
            os.system(f"cd {easy_rsa_dir} && easyrsa revoke {cert_id}")
            os.system(f"cd {easy_rsa_dir} && easyrsa gen-crl")
            print(f"Certificado revogado: {cert_id}")
        except Exception as e:
            print(f"Erro ao revogar certificado: {e}")
        
        return True
        
    except Exception as e:
        print(f"Erro ao deletar certificado: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Uso: python3 deleteScript.py <id_certificado>")
        sys.exit(1)
    
    cert_id = sys.argv[1]
    success = delete_certificate(cert_id)
    sys.exit(0 if success else 1) 