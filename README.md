# OpenVPN Portal 

*A self-service portal for secure, individualized VPN certificate management in organizations.*

[![Python](https://img.shields.io/badge/python-3.7+-blue.svg)](https://www.python.org/)
[![OpenVPN](https://img.shields.io/badge/OpenVPN-2.4%2B-brightgreen)](https://openvpn.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Overview
Traditional VPNs often share a single certificate across all employees, creating security risks during revocations. **SelfCert VPN** solves this by:
- Allowing **employees** to generate/revoke their own OpenVPN certificates.
- Enabling **admins** to manage user access without handling individual certificates.

**Target Users**:
- **Organizations** with remote teams.
- **Sysadmins** tired of manual certificate management.
- **Employees** needing secure, personalized VPN access.

---

## Features
### For Employees
- Generate personalized VPN certificates with one click.
- Revoke own certificates when compromised or unused.
- Download pre-configured `.ovpn` files + certificates.

### For Admins
- Add/remove employees or other admins.
- Revoke access instantly (user-level or certificate-level).
- Audit trail of certificate issuance/revocation.

---

## Quick Start
### Prerequisites
- Debian/Ubuntu server with:
  - OpenVPN (`sudo apt install openvpn`)
  - EasyRSA (`sudo apt install easy-rsa`)
  - Python 3.7+ (`sudo apt install python3`)

### Installation
```bash
git clone https://github.com/MayconLSouza/OpenVPN-Portal.git
cd OpenVPN-Portal
sudo pip install -r requirements.txt  # (if applicable)
