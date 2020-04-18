package http.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonModel{
	@JsonProperty("name")
	private String name;

	@JsonProperty("position")
	private List<String> position;

	@JsonProperty("age")
	private int age;

	public String getName(){
		return name;
	}

	public List<String> getPosition(){
		return position;
	}

	public int getAge(){
		return age;
	}

	public PersonModel setName(String name) {
		this.name = name;
		return this;
	}

	public PersonModel setPosition(List<String> position) {
		this.position = position;
		return this;
	}

	public PersonModel setAge(int age) {
		this.age = age;
		return this;
	}
}