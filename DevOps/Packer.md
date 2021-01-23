# Packer

"HashiCorp Packer allows us [to create VM images] by applying a provisioning run of our CM tool of choice (Ansible) and outputting a ready-to-use image for any of the big cloud providers. By doing this, you could have the desired state of the cluster nodes (or any other server configuration) permanently enshrined in an image, and for any node addition needs for the cluster all you would need to do is spawn more VM instances based on the same Packer image". So it's Docker but for the whole VM.



## Resources

**Books**

- [Deployment with Docker](https://learning.oreilly.com/library/view/deployment-with-docker/9781786469007/ba2dcf13-47f3-4151-a034-40a9918be9b8.xhtml) (short tutorial)



## Vocab

- **AMI image**
  "An Amazon Machine Image is a special type of virtual appliance that is used to create a virtual machine within the Amazon Elastic Compute Cloud. It serves as the basic unit of deployment for services delivered using EC2."
- **CM tool**
  Configuration management tool like Ansible.
- **ISO file**
  "An optical disc image is a disk image that contains everything that would be written to an optical disc, disk sector by disc sector, including the optical disc file system."
- **Paas vs IaaS**
  IaaS provides infrastructure, i.e. hardware. PaaS also known as application platform as a service (aPaaS), is when a company provides a platform to run software. IaaS providers have control of the operating system infrastructure. In contrast, PaaS only allows the provider to control applications and data. So basically bare metal servers? IaaS. VMs? PaaS. Or like IaaS provide hardware to run anything. PaaS provide "containers" to run stuff (e.g. a VM). And SaaS is just some software end product.

