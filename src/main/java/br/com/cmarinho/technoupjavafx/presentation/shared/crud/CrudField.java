package br.com.cmarinho.technoupjavafx.presentation.shared.crud;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class CrudField<T> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String label;
    private final Function<T, String> tableValueProvider;
    private final Function<String, Control> inputFactory;
    private final Function<Control, String> inputValueReader;
    private final Function<String, String> validator;

    private CrudField(
            String label,
            Function<T, String> tableValueProvider,
            Function<String, Control> inputFactory,
            Function<Control, String> inputValueReader,
            Function<String, String> validator) {
        this.label = label;
        this.tableValueProvider = tableValueProvider;
        this.inputFactory = inputFactory;
        this.inputValueReader = inputValueReader;
        this.validator = validator;
    }

    public String label() {
        return label;
    }

    public String tableValue(T entity) {
        return tableValueProvider.apply(entity);
    }

    public FormInput createInput(T entity) {
        String initialValue = entity == null ? "" : tableValue(entity);
        Control input = inputFactory.apply(initialValue);
        return new FormInput(label, input, inputValueReader, validator);
    }

    public static <T> CrudField<T> text(String label, Function<T, String> valueProvider) {
        return requiredText(label, valueProvider);
    }

    public static <T> CrudField<T> requiredText(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> createTextField(initialValue),
                control -> ((TextField) control).getText().trim(),
                value -> validateRequired(label, value));
    }

    public static <T> CrudField<T> password(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> {
                    PasswordField input = new PasswordField();
                    input.setText(initialValue);
                    input.setPrefColumnCount(28);
                    return input;
                },
                control -> ((PasswordField) control).getText().trim(),
                value -> validateRequired(label, value));
    }

    public static <T> CrudField<T> cpf(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> createTextField(initialValue),
                control -> onlyDigits(((TextField) control).getText()),
                value -> validateCpf(label, value));
    }

    public static <T> CrudField<T> cnpj(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> createTextField(initialValue),
                control -> onlyDigits(((TextField) control).getText()),
                value -> validateCnpj(label, value));
    }

    public static <T> CrudField<T> birthDate(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> createDatePicker(initialValue),
                control -> readDatePicker((DatePicker) control),
                value -> validateBirthDate(label, value));
    }

    public static <T> CrudField<T> decimal(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> createTextField(initialValue),
                control -> ((TextField) control).getText().trim(),
                value -> validateDecimal(label, value));
    }

    public static <T> CrudField<T> integer(String label, Function<T, String> valueProvider) {
        return new CrudField<>(
                label,
                valueProvider,
                initialValue -> createTextField(initialValue),
                control -> ((TextField) control).getText().trim(),
                value -> validateInteger(label, value));
    }

    private static TextField createTextField(String initialValue) {
        TextField input = new TextField(initialValue);
        input.setPrefColumnCount(28);
        return input;
    }

    private static DatePicker createDatePicker(String initialValue) {
        DatePicker input = new DatePicker();
        input.setPrefWidth(240);
        input.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate value) {
                return value == null ? "" : DATE_FORMATTER.format(value);
            }

            @Override
            public LocalDate fromString(String value) {
                if (value == null || value.isBlank()) {
                    return null;
                }

                return LocalDate.parse(value, DATE_FORMATTER);
            }
        });

        if (initialValue != null && !initialValue.isBlank()) {
            try {
                input.setValue(LocalDate.parse(initialValue, DATE_FORMATTER));
            } catch (DateTimeParseException exception) {
                input.getEditor().setText(initialValue);
            }
        }

        return input;
    }

    private static String readDatePicker(DatePicker input) {
        LocalDate value = input.getValue();
        return value == null ? input.getEditor().getText().trim() : DATE_FORMATTER.format(value);
    }

    private static String validateRequired(String label, String value) {
        return value == null || value.isBlank() ? label + " e obrigatorio." : null;
    }

    private static String validateCpf(String label, String value) {
        String requiredError = validateRequired(label, value);
        if (requiredError != null) {
            return requiredError;
        }

        if (value.length() != 11 || allDigitsAreEqual(value) || !hasValidCpfDigits(value)) {
            return label + " invalido.";
        }

        return null;
    }

    private static String validateCnpj(String label, String value) {
        String requiredError = validateRequired(label, value);
        if (requiredError != null) {
            return requiredError;
        }

        if (value.length() != 14 || allDigitsAreEqual(value) || !hasValidCnpjDigits(value)) {
            return label + " invalido.";
        }

        return null;
    }

    private static String validateBirthDate(String label, String value) {
        String requiredError = validateRequired(label, value);
        if (requiredError != null) {
            return requiredError;
        }

        try {
            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            return date.isAfter(LocalDate.now()) ? label + " nao pode ser futura." : null;
        } catch (DateTimeParseException exception) {
            return label + " deve estar no formato dd/MM/aaaa.";
        }
    }

    private static String validateDecimal(String label, String value) {
        String requiredError = validateRequired(label, value);
        if (requiredError != null) {
            return requiredError;
        }

        try {
            Double.parseDouble(value);
            return null;
        } catch (NumberFormatException exception) {
            return label + " deve ser um numero.";
        }
    }

    private static String validateInteger(String label, String value) {
        String requiredError = validateRequired(label, value);
        if (requiredError != null) {
            return requiredError;
        }

        try {
            Integer.parseInt(value);
            return null;
        } catch (NumberFormatException exception) {
            return label + " deve ser um numero inteiro.";
        }
    }

    private static String onlyDigits(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean allDigitsAreEqual(String value) {
        char first = value.charAt(0);

        for (int index = 1; index < value.length(); index++) {
            if (value.charAt(index) != first) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasValidCpfDigits(String value) {
        int firstDigit = calculateCpfDigit(value, 9);
        int secondDigit = calculateCpfDigit(value, 10);
        return firstDigit == Character.getNumericValue(value.charAt(9))
                && secondDigit == Character.getNumericValue(value.charAt(10));
    }

    private static int calculateCpfDigit(String value, int length) {
        int sum = 0;

        for (int index = 0; index < length; index++) {
            sum += Character.getNumericValue(value.charAt(index)) * (length + 1 - index);
        }

        int result = 11 - (sum % 11);
        return result > 9 ? 0 : result;
    }

    private static boolean hasValidCnpjDigits(String value) {
        int firstDigit = calculateCnpjDigit(value, new int[] { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 });
        int secondDigit = calculateCnpjDigit(value, new int[] { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 });
        return firstDigit == Character.getNumericValue(value.charAt(12))
                && secondDigit == Character.getNumericValue(value.charAt(13));
    }

    private static int calculateCnpjDigit(String value, int[] weights) {
        int sum = 0;

        for (int index = 0; index < weights.length; index++) {
            sum += Character.getNumericValue(value.charAt(index)) * weights[index];
        }

        int result = sum % 11;
        return result < 2 ? 0 : 11 - result;
    }

    public record FormInput(
            String label,
            Control control,
            Function<Control, String> valueReader,
            Function<String, String> validator) {

        public String value() {
            return valueReader.apply(control);
        }

        public String validate() {
            return validator.apply(value());
        }

        public void requestFocus() {
            control.requestFocus();
        }
    }
}
