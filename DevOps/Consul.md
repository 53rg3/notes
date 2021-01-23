# Concul

Consul is a full-featured service mesh solution. Offers routing, segmentation, failure handling, retries, and network observability. Each of these features can be used individually.



## Todo

- [ ] [Consul vs. Istio](https://www.consul.io/docs/intro/vs/istio)
- [ ] start here: https://learn.hashicorp.com/tutorials/consul/get-started?in=consul/getting-started



## Vocab

- **Service Mesh**
  To run a microservice network you have various problems like:
  - Discovery - How do you know which services are there, which have failed, which were added, etc.
  - Configuration - How to send out a configuration to all services at once?
  - Segmentation - How do make sure that only specific services can talk to each other and that they're part of the network?

- **Sidecar Proxy**
  "The typical way to implement a service mesh is by providing a proxy instance, called a sidecar, for each service instance. Sidecars handle inter‑service communications, monitoring, security‑related concerns – anything that can be abstracted away from the individual services". So basically it does all the fluff which your application would have to do.

