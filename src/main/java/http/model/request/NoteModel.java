package http.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)
public class NoteModel {
	@JsonProperty("notes")
	private String notes;

	@JsonProperty("name")
	private String name;

	@SneakyThrows
	@Override
	public String toString() {
		return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}
}