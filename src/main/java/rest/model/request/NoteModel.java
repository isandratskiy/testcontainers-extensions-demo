package rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoteModel {
	@JsonProperty("notes")
	private String notes;

	@JsonProperty("name")
	private String name;

	public NoteModel setNotes(String notes){
		this.notes = notes;
		return this;
	}

	public NoteModel setName(String name){
		this.name = name;
		return this;
	}
}