package asw.rest.hello;

import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
	@NamedQuery(name="Artist.findAll", query="SELECT a FROM Artist a"),
    @NamedQuery(name="Artist.findByName",
                query="SELECT a FROM Artist a WHERE a.name= :name")
}) 
@XmlRootElement(name = "artist")
public class Artist {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String country;

	
	public Artist(){
	}
	
	public Artist(String name, String country) {
		this.name=name;
		this.country = country;
	}

	public Long getId() {
		return id;
	}
	@XmlElement
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}
	
	@XmlElement
	public void setCountry(String country) {
		this.country = country;
	}


	
	public String toString(){
		   final StringBuilder sb = new StringBuilder();
	        sb.append("Artist"); 
	        sb.append("{id=").append(id); 
	        sb.append(", name='").append(this.name); 
	        sb.append(", country='").append(this.country);  
	        sb.append("}\n");
	        return sb.toString();
	}

}
