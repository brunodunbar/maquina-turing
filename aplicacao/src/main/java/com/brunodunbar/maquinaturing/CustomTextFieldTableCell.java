package com.brunodunbar.maquinaturing;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;


public class CustomTextFieldTableCell<S, T> extends TableCell<S, T> {


    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
            final StringConverter<T> converter) {
        return list -> new CustomTextFieldTableCell<S, T>(converter);
    }


    private TextField textField;


    public CustomTextFieldTableCell() {
        this(null);
    }

    public CustomTextFieldTableCell(StringConverter<T> converter) {
        this.getStyleClass().add("text-field-table-cell");
        setConverter(converter);
    }

    private ObjectProperty<StringConverter<T>> converter =
            new SimpleObjectProperty<StringConverter<T>>(this, "converter");

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }


    @Override
    public void startEdit() {
        if (!isEditable()
                || !getTableView().isEditable()
                || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            if (textField == null) {
                textField = CellUtils.createTextField(this, getConverter());
                textField.textProperty().addListener((ov, oldValue, newValue) -> {
                    if (textField.getText().length() > 1) {
                        String s = textField.getText().substring(0, 1);
                        textField.setText(s);
                    }
                });
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        if (converter == null) {
                            throw new IllegalStateException(
                                    "Attempting to convert text input into Object, but provided "
                                            + "StringConverter is null. Be sure to set a StringConverter "
                                            + "in your cell factory.");
                        }
                        commitEdit(getConverter().fromString(textField.getText()));
                    }
                });
            }

            CellUtils.startEdit(this, getConverter(), null, null, textField);
        }
    }


    @Override
    public void cancelEdit() {
        super.cancelEdit();
        CellUtils.cancelEdit(this, getConverter(), null);
    }


    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        CellUtils.updateItem(this, getConverter(), null, null, textField);
    }
}
