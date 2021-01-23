# Terraform

Provisioning of infrastructure as code. Automate and manage your infrastructure. The "providers" are basically adapters to the APIs of IaaS and the script in "Terraform language" is the API of the adapter. 



## Todo

- [ ] How to create a virtual network? Is that even a Terraform task?
- [ ] Check out the language, see links, to see what's possible 
- [ ] Can we use VirtualBox instead of Hetzner? See [here](https://blog.opennix.ru/posts/use_terraform_with_virtualbox/). Maybe also this: [Experimenting Locally with Terraform](https://stackoverflow.com/questions/39211000/experimenting-locally-with-terraform)



## DRAFT

General tips & nice2knows:

- Don't put your provider credentials into your Terraform configuration. Or that shit will be on GitHub.
- Terraform script are "descriptive" NOT "imperative", i.e. they "describe" (specify) the state in which you want your cluster to be. So if you run a script once and re-run it again without making changes then Terraform will do nothing, because the state of your cluster complies to the specification in your script already.





## Commands

- `terraform init` - Initialize project, checks for and downloads configured provider plugins.
- `terraform plan` - Shows you what will be done after you run your script, what variables will be available (e.g. IP address)
- `terraform apply` - Executes your script



## Vocab

- **Provider**
  Plugins that allow you talk to an API of some IaaS provider. 



## Resources

- Docs, [tutorials](https://www.terraform.io/docs/configuration/index.html)
- Docs, [Terraform language](https://www.terraform.io/docs/configuration/index.html)



## How To

### Install

- Download newest version and extract.

- Move to `/usr/local/bin`

  ```
  sudo mv terraform /usr/local/bin
  ```

- Running `terraform -v` should print version

