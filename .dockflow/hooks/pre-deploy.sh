#!/bin/bash
# Write SSH private key used by var-mohistmc.mount (SSHFS to Oracle VM)
mkdir -p /home/{{ current.user }}/.ssh
printf '%s' '{{ current.env.ssh_private_key_vm_oracle }}' > /home/{{ current.user }}/.ssh/SSH_PRIVATE_KEY_VM_ORACLE
chmod 600 /home/{{ current.user }}/.ssh/SSH_PRIVATE_KEY_VM_ORACLE

# Reload systemd and ensure services are running before app starts
sudo systemctl daemon-reload
sudo systemctl enable --now rclone-s3-mohistmc.service
sudo systemctl enable --now var-mohistmc.mount
