# OpenStack

Seems like a complete tool set to run a VPS company. You can create VPS on servers via images, configure networks between the VPS, etc.
"OpenStack is a free open standard cloud computing platform, mostly deployed as infrastructure-as-a-service in both public and private clouds where virtual servers and other resources are made available to users."



## Links & Resources

### Books

- [OpenStack Essentials](https://learning.oreilly.com/library/view/openstack-essentials-/9781786462664/ch01.html)

### Videos

- [ ] [OpenStack Basics - An overview for the Absolute Beginner](https://www.youtube.com/watch?v=8kADjGCuSVI)

- [ ] [Is Kubernetes the New OpenStack?](https://www.youtube.com/watch?v=eET-1pb_xXI)

- [ ] [What is OpenShift?](https://www.youtube.com/watch?v=KTN_QBuDplo)

- [ ] [OpenStack Basics - Overview](https://www.youtube.com/watch?v=c1GFoY4btpo)

- [ ] [Why Use Open Stack For Developers & Business Solutions | vSphere](https://www.youtube.com/watch?v=Bk4NoUsikVA)





## Questions

- "Each component in OpenStack manages a different resource that can be virtualized for the end user." -> virtualized?
  Basically "sectioning off" physical resources to be used individually, like a VM sections off parts of your computer. You can do that with networks, hard-drives, etc.



## Vocab

- **private cloud**
- **public cloud**
- **network functions virtualization**

- OpenStack components, see [here](https://www.redhat.com/en/topics/openstack)
  - Undercloud / Overcloud
  - Nova - provisions VPS instances
  - Neutron - manages virtual networks
  - Swift - manages object storages
  - Cinder - manages block storages
  - Keystone - manages authentication between services
  - Glance - stores OS images 



- **Block Storage / Object Storage** 
  Block storage is an approach to data storage in which each storage volume acts as an individual hard drive that is configured by the storage administrator. In the block storage model, data is saved to the storage media in fixed-sized chunks called blocks.
  Object storage is storing data as "objects", which come with more meta data. This comes in handy when you have to deal with huge data volumes (e.g. hundreds of TB), because "Object-based storage architectures can be scaled out and managed simply by adding additional nodes".
- **Ceilometer**
  Ceilometer is the telemetry component. It collects resource measurements and is able to monitor the cluster. Ceilometer meters measure the resources being used in an OpenStack deployment.
- **Cinder**
  Cinder is the block storage management component. Volumes can be created and attached to instances. Then they are used on the instances as any other block device would be used. On the instance, the block device can be partitioned and a filesystem can be created and mounted. Cinder also handles snapshots.
- **Heat**
  Heat is the orchestration component. Orchestration is the process of launching multiple instances that are intended to work together. In orchestration, there is a file, known as a template, used to define what will be launched.
- **Horizon / Dashboard**
  OpenStack Horizon is a web-based graphical interface that cloud administrators and users can access to manage OpenStack compute, storage and networking services.
- **Glance**
  Glance image services include discovering, registering, and retrieving virtual machine images. Glance has a RESTful API that allows querying of VM image metadata as well as retrieval of the actual image.
  Glance is the image management component. Before a server is useful, it needs to have an operating system installed on it. This is a boilerplate task that cloud computing has streamlined by creating a registry of pre-installed disk images to boot from. Glance serves as this registry within an OpenStack deployment.
- **Keystone**
  Keystone is the identity management component. The first thing that needs to happen while connecting to an OpenStack deployment is authentication. In its most basic installation, Keystone will manage tenants, users, and roles and be a catalog of services and endpoints for all the components in the running cluster. 
  By default, Keystone uses username/password authentication to request a token and the acquired tokens for subsequent requests. All the components in the cluster can use the token to verify the user and the user's access.
- **Neutron**
  Neutron is the network management component. With Keystone, we're authenticated, and from Glance, a disk image will be provided. The next resource required for launch is a virtual network. Neutron is the management tools for these virtual network.
- **Nova** (OpenStack Compute)
  Nova is the OpenStack project that provides a way to provision compute instances (aka virtual servers). Nova supports creating virtual machines, baremetal servers (through the use of ironic), and has limited support for system containers.
- **RDO**
  RDO is a freely-available, community-supported distribution of OpenStack that was launched by Red Hat in 2013. RDO stands for "RPM Distribution of OpenStack".
- **RPM**
  Red Hat Package Manager. RPM Package Manager is a free and open-source package management system.
- **Tenant**
  Everything in OpenStack must exist in a tenant. A tenant is simply a grouping of objects. Users, instances, and networks are examples of objects. They cannot exist outside of a tenant. Another name for a tenant is a project.
- **Software Defined Networking**
  Software-Defined Networking (SDN) is a network architecture approach that enables the network to be intelligently and centrally controlled, or 'programmed,' using software applications. This helps operators manage the entire network consistently and holistically, regardless of the underlying network technology.
- **Swift**
  Swift is the object storage management component. See object storage above. You don't need that.
- **Virtual Network**
  A virtual network is a network that emulates a physical network and is a combination of hardware and software network resources. So it's like a physical network, but sectioned off by software. Just like a VM sections off a part of your physical computer's resources.
- **vSwitch**
  Open vSwitch is a virtual managed switch for virtual networks. As long as the nodes in your cluster have simple connectivity to each other, Open vSwitch can be the infrastructure configured to isolate the virtual networks for the tenants in OpenStack.

