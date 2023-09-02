package services;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import jakarta.ws.rs.core.MediaType;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;

import repositories.MonitorRepository;
import repositories.OrderRepository;
import repositories.UserRepository;

import java.io.IOException;
import java.util.*;

public class RestfulServer {
    public static void main(String[] args) throws IOException {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();

        factoryBean.setResourceClasses(
                OrderRepository.class,
                UserRepository.class,
                MonitorRepository.class
        );

        List<ResourceProvider> resourceProviders = Arrays.asList(
            new SingletonResourceProvider(new OrderRepository()),
            new SingletonResourceProvider(new UserRepository()),
            new SingletonResourceProvider(new MonitorRepository())
        );

        factoryBean.setResourceProviders(resourceProviders);

        Map<Object, Object> extensionMappings = new HashMap<Object, Object>() {{
            put("xml", MediaType.APPLICATION_XML);
            put("json", MediaType.APPLICATION_JSON);
        }};

        factoryBean.setExtensionMappings(extensionMappings);

        List<Object> providers = new ArrayList<Object>() {{
            add(new JAXBElementProvider<>());
            add(new JacksonJsonProvider());
        }};

        factoryBean.setProviders(providers);

        factoryBean.setAddress("http://localhost:8080/");
        Server server = factoryBean.create();

        System.out.println("Server ready...");
        System.in.read();
        server.destroy();
        System.exit(0);
    }
}
