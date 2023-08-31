package services;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import repositories.ItemTypeRepository;
import repositories.MonitorRepository;
import repositories.OrderRepository;
import repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class RestfulServer {
    public static void main(String[] args) {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();

        factoryBean.setResourceClasses(
                ItemTypeRepository.class,
                OrderRepository.class,
                UserRepository.class,
                MonitorRepository.class
        );

        List<ResourceProvider> resourceProviders = new ArrayList<>();
        resourceProviders.add(new SingletonResourceProvider(new ItemTypeRepository()));
        resourceProviders.add(new SingletonResourceProvider(new OrderRepository()));
        resourceProviders.add(new SingletonResourceProvider(new UserRepository()));
        resourceProviders.add(new SingletonResourceProvider(new MonitorRepository()));

        factoryBean.setResourceProviders(resourceProviders);

        factoryBean.setAddress("http://localhost:8080/");
        factoryBean.create();
    }
}
