package jp.ac.hsc.fxsample;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ButtonCellFactory<E> implements Callback<TableColumn<E, ?>, TableCell<E, ?>> {

	  private String label;
	  private Consumer<E> onClick;

	  /**
	   * @param label ボタンに表示するテキスト
	   * @param onClick ボタンがクリックされたときに呼び出される
	   */
	  public ButtonCellFactory(String label, Consumer<E> onClick) {
	    this.label = label;
	    this.onClick = onClick;
	  }

	  @Override
	  public TableCell<E, ?> call(TableColumn<E, ?> param) {
	    return new TableCell<E, Object>() {
	      @Override
	      public void updateItem(Object item, boolean empty) {
	        super.updateItem(item, empty);
	        if (empty) {
	          setGraphic(null);
	          setText(null);
	        } else {
	          Button btn = new Button(label);
	          btn.setOnAction(event -> {
	            E value = getTableView().getItems().get(getIndex());
	            onClick.accept(value);
	          });
	          setGraphic(btn);
	          setText(null);
	        }
	      }
	    };
	  }
	}