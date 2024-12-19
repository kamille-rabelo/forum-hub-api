package forum.hub.api.domain.answer;

public record AnswerViewDTO(
        String author,
        String message,
        Boolean solution) {

    public AnswerViewDTO (Answer answer) {
        this(answer.getAuthor().getName(), answer.getMessage(), answer.getSolution());
    }
}
