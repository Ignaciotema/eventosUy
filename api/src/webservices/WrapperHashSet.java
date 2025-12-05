package webservices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hashSetWrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrapperHashSet<T> {
    
    @XmlElement(name = "item")
    private List<T> items;
    
    public WrapperHashSet() {
        this.items = new ArrayList<>();
    }
    
   public HashSet<T> toHashSet() {
		return new HashSet<>(items);
	}
    public WrapperHashSet(Set<T> set) {
        this.items = new ArrayList<>(set);
    }
    
 
}