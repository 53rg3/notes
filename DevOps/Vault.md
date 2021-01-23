# Vault

Encrypt and decrypt stuff. 

- Offers a server to store and retrieve stuff and an API to do it programmatically.
- Allows different types of users and different permissions to do stuff (read, write, etc)

![image-20210118191635665](/home/cc/Desktop/Programming/notes/DevOps/_res/Vault/image-20210118191635665.png)

## Need2Knows

- Vault uses ENV variables for it's settings. E.g. you need to set `export VAULT_ADDR='http://127.0.0.1:8200'` for Vault to know its server.



## Commands

- **vault kv**

  ```bash
  vault kv put secret/hello bar=hello
  vault kv get secret/hello
  ```

  Put key-value pair `bar=hello` with Secrets Engine `kv` configured with `secret/` at key `hello` and the respective get command.  If you would want to use another path than `secret/` then you would need to configure a Secrets Engine for such path (see "vault secrets enable").

- **vault secrets enable / disable**

  ```
  vault secrets enable -path=super_secret kv
  vault secrets disable super_secret/
  ```

  Enables Secret Engine `kv` at path `super_secret/`. Note that disabling a path also REMOVES its data.



## Nice2Knows

- You should store values written into a CLI command, e.g. `vault kv put secret/hello foo=world`. Shell history will show them. Use files and store their content as values.
- There's some tool called [path-help](https://learn.hashicorp.com/tutorials/vault/getting-started-help).



## Vocab

- **Dynamic Secrets**
  Dynamic secrets are generated when they are accessed. Dynamic secrets do not exist until they are read, so there is no risk of someone stealing them or another client using the same secrets. E.g. you dynamically create an IAM user on AWS when needed and delete it after work is done.
- **Policy**
  Policies in Vault control what a user can access.
- **Secrets Engine**
  Vault uses Secrets Engines to store data. That's basically an encryption program to which you connect to from some path. Execute `vault secrets list` to see all configured engines.



## Links & Resources

- Official [tutorial](https://learn.hashicorp.com/tutorials/vault/getting-started-install?in=vault/getting-started) with very nice videos
- [Docs](https://www.vaultproject.io/docs)



## Cookbook

### How to install

Download from [website](https://www.vaultproject.io/downloads). Set binary to be accessible from `$PATH`:

```
export PATH=$PATH:/path/to/vault/DIRECTORY
```

## How to create a run script

Create `run_vault.sh`:

```bash
export VAULT_SKIP_VERIFY=true
export VAULT_ADDR="https://your-vault-server.com:8200"
vault login -method=userpass \
    username=username \
    password="password"
```

Note that the `vault` command must be from `$PATH`. Put that run script also in `$PATH`.