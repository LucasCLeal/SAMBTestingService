# SAMBTestingService
Self-adapt model based testing service. A masters theses project developed by Lucas Carvalho Leal at UNICAMP with collaboration with UNIFI.

The objective of the project is to develop two new webservices to interact with a testing service, witch is in charge to execute tests over some SOA Applications. The project has two new components, a ModelGenerator WS and a ModelBasedTesting WS. 
The first one is responsible to keep track of the SOA applications that are covered by the testing service and generate a model representation of the composition. The model is a simple xml file, the extension fo the file is .graphml and can be edited using any tool that can open the file like (Yed - https://www.yworks.com/products/yed). The second webservice is a responsible to recieve the model and generate online testcases from it, this services will be execute by the testing service in order to validate the proper behaviour of the SOA Application.
