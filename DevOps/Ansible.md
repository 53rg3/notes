# Ansible

"Ansible is an IT automation tool. It can configure systems, deploy software, and orchestrate more advanced IT tasks such as continuous deployments or zero downtime rolling updates."
Basically it's a bunch of adapters for command line instructions which are configured via YAML files in a standardized format. So instead of having to deal with a list of obscure CLI instructions, endless arguments lists and other quirks, you can just define instructions (tasks) in a neatly formatted way once and can reuse them with single unified command through Ansible.



## Todo

- [ ] Debugging. See Ansible_OLD & docs. Save results as var.
- [ ] Ansible Vault



## Writing Playbooks

### File Structure with roles

If you put all tasks into the Playbook file, then you can just run this. Otherwise you need the following file structure:

```yaml
roles/ 				# directory for all roles
	some-role/  	# a role, name of directory is used in Playbook
playbook.yml		# your Playbook
play.sh				# executable for the ansible-playbook command with arguments
```

See also [docs](https://docs.ansible.com/ansible/latest/user_guide/playbooks_reuse_roles.html#role-directory-structure) for file structure of roles.

### Execution order

See [here](/home/cc/Desktop/Programming/JWS/Lts/projects/notes/2021/01_Jan/prometheus/ansible-playbook/roles/config-roles/config-one). When you use the `roles` option at the play level, Ansible treats the roles as static imports and processes them during playbook parsing. Ansible executes your playbook in this order:

- Any `pre_tasks` defined in the play.
- Any handlers triggered by pre_tasks.
- Each role listed in `roles:`, in the order listed. Any role dependencies defined in the role’s `meta/main.yml` run first, subject to tag filtering and conditionals. See [Using role dependencies](https://docs.ansible.com/ansible/latest/user_guide/playbooks_reuse_roles.html?#role-dependencies) for more details.
- Any `tasks` defined in the play.
- Any handlers triggered by the roles or tasks.
- Any `post_tasks` defined in the play.
- Any handlers triggered by post_tasks.



## Debugging

- If running `ansible` fails, you can get more verbose output with adding argument `-v`, `-vv`, `-vvv`, `-vvvv` (each is more verbose)



## Encountered Problems

**Failed to connect to the host via ssh: cc@192.168.178.27: Permission denied (publickey,password).**
By default Ansible tries to connect via SSH key. If no suitable one is available then Ansible will fail. So you either need to copy yours to the hosts or, in case you're just playing around, use password identification.

Copy SSH key: 

```
ssh-copy-id -i ${KEY} -p ${PORT} ${USER}@${HOST}
```

Use password (`-e` like extra variables):

```
ansible-playbook -e "ansible_ssh_pass=123" ...
```



## Cookbook

### How to install a role to local folder

Regular install command installs to `~/.ansible/roles`

```
ansible-galaxy install \
	--roles-path=/some/absolute/path \
	cloudalchemy.prometheus
```



### Prompt with enum options and default with asserts

```yaml
  vars:
    config_type_options:
      - "dot_bot"
      - "lts_internal"

  # Prompt will not be shown if 'config_type' is set as argument, e.g. `--extra-vars "config_type=dot_bot"`
  vars_prompt:
    - name: config_type
      prompt: "Which config shall be used? Options: {{ config_type_options }}, default is"
      private: no
      default: "{{ config_type_options[0] }}"

  tasks:
  - name: "Assert 'config_type' is set"
    assert:
      that:
        - config_type is defined
      fail_msg: "Variable 'config_type' must be set"
  - name: "Assert 'config_type' is one of {{ config_type_options }}"
    assert:
      that:
        - config_type in config_type_options
      fail_msg: "Variable 'config_type' must be one of {{ config_type_options }}, got: '{{ config_type }}'"
      success_msg: "Prometheus will be configured for {{ config_type }}"
```



## Connect via SSH with password

```bash
ansible-playbook \
    --ask-become-pass \
    --inventory cc@192.168.178.27, \
    -e "ansible_ssh_pass=123" \
    --verbose \
    playbook.yml
```



## Links & Resources 

**Books**

- [Ansible 2 Cloud Automation Cookbook](https://learning.oreilly.com/library/view/ansible-2-cloud/9781788295826/)
- 



## Vocab

- **Ansible Galaxy**
  CLI tool to create `roles` which create the standardized file structure for it. All directories are optional and you should delete those you don't need. There's also a [website](https://galaxy.ansible.com/search) with premade roles. 
- **Handler**
  Same as a `task` (it can do anything a Task can), but it will only run when called by another Task. You can think of it as part of an event system. A Handler will take an action when called by an event it listens for. It's basically like a function which you can run repeatedly when needed. This comes in handy when you need a command multiple times in your role, e.g. `sudo service nginx restart`
- **Hosts** (Inventory)
  The hosts file contains lists of your hosts where you want a role to execute. The file can contain multiple lists of hosts which are identified by an alias (name). See [docs](https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html).
- **Playbook**
  A file which serves as entry point for Ansible where you declare which `roles` (these contain `tasks`) on which `hosts` shall be run. And some other settings e.g. `become` to change the user when on the server. 
- **Role**
  A standardized file structure (see [docs](https://docs.ansible.com/ansible/latest/user_guide/playbooks_reuse_roles.html#role-directory-structure)) which consists of a set of `tasks` and their required assets (e.g. config file templates, variables, etc). For example "deploy my cool app" could be a role, where Ansible would upload your app, install Nginx to serve it and configure SSH and UFW to secure the server. Although "install Nginx" could also be a role of its own. 
  - You can get premade roles on [Ansible Galaxy](https://galaxy.ansible.com/search), all major tools have their own repository.
  - Roles can depend on each other, defined in `meta/main.yml`.  E.g. Nginx Role can depend on the SSL Role, which installs SSL certificates. See [docs](https://docs.ansible.com/ansible/latest/user_guide/playbooks_reuse_roles.html#using-role-dependencies) for details.

- **Template**
  Template files can contain template variables, based on Python's Jinja2 template engine. For example you can create templates for config files for a role, which need some specific variables. The markers for those variables (e.g. `path: '{{ somePath }}'`) in the template file are then replaced by Ansible while it runs with your desired values. If a file is "static" (i.e. it doesn't contain variables), then you should place into the `/files` directory of your role.
- **Variable**
  If you have values which need to be used multiple times in a role or which can differ depending on how you want your role to work, then you need variables. These are either provided as CLI arguments when running Ansible or in files. Must be declared in YAML format, see [docs](https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html).
  - `register:` directive
    When running a command you can register its result as variable. E.g. `register: result_of_command` can then be used conditionally in another command via `when: result_of_task == 5`. See [docs](https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html#registering-variables) for details. What is registered as variable depends on the module you're using. It can be simple values, lists, nested objects, etc.
  - `defaults/main.yml` has a lower priority than `vars/main.yml`, i.e. variables sharing a name will be overridden, see [docs](https://docs.ansible.com/ansible/latest/user_guide/playbooks_reuse_roles.html#role-directory-structure).

