<!--- FILE_TOC -->

# Todo

- How to run on non-standard SSH port?
- The “Vault” is a feature of Ansible that allows you to keep sensitive data such as passwords or keys protected at rest, rather than as plaintext in playbooks or roles.
- Can we rollback? https://serverfault.com/a/935149/268088
- How to snapshot a VM after every step for rollback?
- Check if [h5bp](https://github.com/h5bp/server-configs-nginx/tree/master/h5bp) nginx config is of any benefit



# Vocab

- **Playbooks** can run multiple Tasks and provide some more advanced functionality that we would miss out on using ad-hoc commands.
- A **Handler** is exactly the same as a **Task** (it can do anything a Task can), but it will only run when called by another Task. You can think of it as part of an Event system; A Handler will take an action when called by an event it listens for.
- **Roles**
  Roles are good for organizing multiple, related Tasks and encapsulating data needed to accomplish those Tasks. For example, installing Nginx may involve adding a package repository, installing the package, and setting up configuration. We've seen installation via a Playbook, but once we start configuring our installations, the Playbooks tend to get a little more busy.
  :heavy_exclamation_mark::heavy_exclamation_mark: roles can depend on each other. Via dependencies defined in `meta/main.yml`. E.g. Nginx Role can depend on the SSL Role, which installs SSL certificates.
- **`register: foo_result`** in playbook
  Another major use of variables is running a command and registering the result of that command as a variable. When you execute a task and save the return value in a variable for use in later tasks, you create a registered variable. This can then later be reused. E.g. `register: result_of_some_task` can then be used conditionally in another task via `when: result_of_some_task|success`. 
  For example, registering the result of the command run via the `shell` module can let you access the stdout of that command.
- `ansible-galaxy`
  Creates a scaffold for a role or let's you download community created roles.
- **Templates**
  Template files can contain template variables, based on Python's Jinja2 template engine. Files in here should end in .j2, but can otherwise have any name. I.e. you can place your config files here which include variables which are then replaced by somewhere else defined variables.



# Examples

## Install Nginx with a single command

```bash
ansible -i ./hosts all -b --ask-become-pass --become-user=root -m apt -a 'name=nginx state=installed update_cache=true'
```

- `-i ./hosts`
  Set the inventory file, the one named hosts
- `-b`
  "become", tell Ansible to become another user to run the command
- `--ask-become-pass`
  Will ask for the sudo password. If not provided 
- `--become-user=root`
  Run the following commands as user "root" (e.g. use "sudo" with the command) local | remote - Run on local or remote defined hosts from the inventory file
- `-m apt`
  Use the apt module
- `-a 'name=nginx state=installed update_cache=true'`
  Provide the arguments for the apt module, including the package name, our desired end state and whether to update the package repository cache or not

## Install Nginx via Playbook

Same as above, but as Playbook. See `examples/1-install-nginx`.

```yaml
---
- hosts: all
  become: yes
  become_user: root

  tasks:
   - name: Install Nginx
     apt:
       name: nginx
       state: present
       update_cache: true
```

Execute with command below. Note that we're using `--ask-become-pass`

```bash
ansible-playbook -i hosts install-nginx --ask-become-pass
```

## Using handlers (subtasks)

Same as above, but the handler `stop_nginx` will set Nginx to the stopped state via `service`. See ``examples/2-install-nginx-then-stop-it`

```yaml
---
- hosts: all
  become: yes
  become_user: root

  tasks:
   - name: Install Nginx
     apt:
       name: nginx
       state: present
       update_cache: true
     notify:
       - stop_nginx

  handlers:
   - name: stop_nginx
     service:
       name: nginx
       state: stopped
```

Run with:

```bash
ansible-playbook -i hosts install-nginx-then-stop-it --ask-become-pass
```

## Using variables

Same as above, but now we're using a variable to creating folders. We also `register` the result of a task and on the condition that it was successful we execute a handler. See ``examples/3-create-web-root`.

```yaml
---
- hosts: all
  become: yes
  become_user: root

########### VARS #############
  vars:
   - docroot: /var/www/muh_website/public

########### TASKS ############
  tasks:
   - name: Add Nginx Repository
     apt_repository:
       repo: ppa:nginx/stable
       state: present
     register: ppastable

   - name: Install Nginx
     apt:
       pkg: nginx
       state: present
       update_cache: true
     when: ppastable is success
     notify:
      - Start Nginx

   - name: Create Web Root
     file:
      path: '{{ docroot }}'
      mode: '775'
      state: directory
      owner: www-data
      group: www-data
     notify:
      - Reload Nginx

########### HANDLERS ###########
  handlers:
   - name: Start Nginx
     service:
       name: nginx
       state: started

   - name: Reload Nginx
     service:
       name: nginx
       state: reloaded
```

Notes:

- Use `mode` for directories with quotes. Otherwise you can get unexpected results, because they are interpreted as octal numbers (you can also omit the quotes, but the number has to start with 0, i.e. 0775)
- `muh_website/public` creates the whole paths inclusive sub-folders.

Run with:

```bash
ansible-playbook -i hosts install-nginx-then-stop-it --ask-become-pass
```

## Using roles

Shit load of stuff, see `examples/4-roles`.

**Notes:**

- **nginx config file**
  - Uses `include h5bp/basic.conf;` which includes more settings. 
  - The SSL settings are simply paths to the SSL cert & key. Commented out. The `h5bp/directive-only/ssl.conf` also doesn't exist, so also commented out.
- **Tasks**
  - Add the nginx/stable repository
  - Install & start Nginx
  - Add H5BP configuration files
  - Disable the default Nginx configuration by removing the symlink to the `default` file from the `sites-enabled` directory
  - Copy the `serversforhackers.com.conf.j2` virtual host template into the Nginx configuration, rendering the template as it does
  - Enable the Nginx server configuration by symlinking it to the `sites-enabled` directory
  - Create the web root directory
  - Change permission (recursively) for the project root directory, which is one level above the web root created previously



# Create a new Playbook

Create the scaffold

```bash
mkdir your-playbook
cd your-playbook
mkdir roles
touch hosts
touch playbook
```

Example hosts file

```
[mynetwork]
192.168.178.27
192.168.178.28
192.168.178.29
```

Example playbook file

```YAML
---
- hosts: all
  become: yes
  become_user: root
  roles:
    - nginx
```





# Hosts file

Hosts for Ansible can be specified in an external file other than the default one. Example file:

```
[johnswebsitecom]
192.168.178.25
```

Only use `a-Z0-9` in the host alias. Call specific host via:

```
ansible -i host_file johnswebsitecom -m ping
```

Get `roles` via `ansible-galaxy`. Don't forget to define them in the playbook file.

```
ansible-galaxy init nginx
```

# Run Playbook  with without host file

Notice the comma at the end of inventory. Without it won't work.

```bash
ansible-playbook --user=YOUR_USER --ask-become-pass --inventory="192.168.178.25," playbook
```



# See what Ansible gathered as facts about the system 

https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html#variables-discovered-from-systems-facts



# Debugging

Make a debug task:

```
- name: Get nologin path
  command: which nologin
  register: nologin
```

And declare the var you want to inspect:

```
- debug:
    var: nologin
```

Debug info will be printed after run.



# ansible-vault

## Running playbooks with vault variables

Run with `ansible-playbook --ask-vault-pass`


## Encrypt a whole file

To encrypt a file use:

```bash
ansible-vault encrypt YOUR_FILE.yml
```

To edit an encrypted file use:

```
EDITOR=nano ansible-vault edit YOUR_FILE.yml
```

To decrypt a file use:

```bash
ansible-vault decrypt YOUR_FILE.yml
```

## Encrypt a single string

```
ansible-vault encrypt_string '123456' --name 'password'
```

Copy the whole output with the variable name into your desired destination.

## Using vars for passwords

Using 

```
password: "{{ PASS_VARIABLE | password_hash('sha512') }}"
```

in Ansible 2.8.3 causes error:

```
fatal: [192.168.178.25]: FAILED! => {"msg": "Unexpected templating type error occurred on ({{ PASS_VARIABLE | password_hash('sha512') }}): crypt() argument 1 must be string, not AnsibleVaultEncryptedUnicode"}
```

Workaround is by using pipe to `string`, see https://github.com/ansible/ansible/issues/24425

```
password: "{{ PASS_VARIABLE | string | password_hash('sha512') }}"
```

