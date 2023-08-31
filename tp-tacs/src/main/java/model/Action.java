package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action")
public interface Action {

    void notify(Monitor monitor);
    String getDescription();

}