# Ubuntu



## SSH Keys

### List available keys

```bash
gpg --list-secret-keys --keyid-format LONG
```



## Network

### Show network interfaces

```bash
ip a
```



### Change DNS

```
sudo nano /etc/resolv.conf
```

Put your `nameserver 8.8.8.8` or whatever in it. No refreshing needed. 

Verify:

```bash
> dig google.com

; <<>> DiG 9.16.1-Ubuntu <<>> any google.com
// ...
;; Query time: 19 msec
;; SERVER: 8.8.8.8#53(8.8.8.8)
```



### Check IP as JSON

```
curl https://api.myip.com/
```





## Manage software



### Uninstall Software

- Use with `--purge` to also delete config files etc.

  ```bash
  sudo apt-get --purge remove postgresql
  ```

- List all installed packages with a certain name

  ```bash
  dpkg -l | grep "postgr.*"
  ```

  

