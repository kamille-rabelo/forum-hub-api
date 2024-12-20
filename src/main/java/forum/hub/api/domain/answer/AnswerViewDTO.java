package forum.hub.api.domain.answer;

public record AnswerViewDTO(
        Long id,
        String author,
        String message,
        Boolean solution) {

    public AnswerViewDTO (Answer answer) {
        this(answer.getId(), answer.getAuthor().getName(), answer.getMessage(), answer.getSolution());
    }
}
