package tools.util;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
public class SampleController  implements Initializable{
	 private static int maxRow = 999;
	 private String columnName;
	 StringBuffer sBuff= new StringBuffer();
	 
	 @FXML TextArea textArea1;
	 @FXML TextArea textArea2;
     @FXML Button button1;
     @FXML Button button2;
     @FXML Button buttonPaste;
     @FXML Label label1;
     TextField textField2 = new TextField();
     @FXML AnchorPane mainPane;
     FlowPane fp ;
     Dialog<ButtonType> dd;
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// TODO Auto-generated method stub
		initButton1();
		label1.setText("提示： 对于mac用户从excel复制过来的数据请点击粘贴来粘贴数据防止数据无法换行 \n"
				+ "对于oracel数据库用户请点击oracel生成 超过1000条需要输入columnName \n");
	        dd = new Dialog<ButtonType>();
	        dd.getDialogPane().getButtonTypes().add(ButtonType.OK);
	        dd.setWidth(400);
	        dd.setHeight(300);	        
			dd.setContentText("请输入oracle要查询的列名");		
			
			GridPane root = new GridPane();
			root.setVisible(false);
			root.setMaxWidth(Double.MAX_VALUE);
			root.add(new Label("超过一千行请输入oracle要查询的列名"),0,0);
			root.add(textField2, 0, 1);
			root.setVisible(true);
			root.setPrefSize(300, 200);

			textField2.setPrefWidth(50);
			textField2.setPrefHeight(20);
			textField2.setText("");
			textField2.setVisible(true);
			dd.getDialogPane().setContent(root);
	}

	public void initButton1(){    	 
    	 button1.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				if(event.getEventType()==MouseEvent.MOUSE_RELEASED){					
				String sourceText =	textArea1.getText();
				genText( sourceText);					
				}
				
			}});
    	 
    	 button2.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
 			public void handle(MouseEvent event) {
 				if(event.getEventType()==MouseEvent.MOUSE_RELEASED){
 				columnName="";
 				String sourceText =	textArea1.getText();  
     			genOracleText( sourceText);					
 				}				
 			}});
    	 
    	 
    	 buttonPaste.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
 			public void handle(MouseEvent event) {
 				if(event.getEventType()==MouseEvent.MOUSE_RELEASED){	 	
 					String sourceStr = getClipboardText();
 					String sourceArray[] = sourceStr.split("\n");
 			    	if(sourceArray.length==1){
 			    		sourceArray = sourceStr.split("\r");
 			    	}
 					 for(String temp:sourceArray){  
 			    		 sBuff.append(temp);
 			    		 sBuff.append("\n");				
 			    	 }
 					textArea1.setText(sBuff.toString());	
 					sBuff.delete(0, sBuff.length());
 				}
 				
 			}});
    	 
     }
     
     public void genText(String sourceText){
    	 if(sourceText.trim()==""){
    		 return;
    	 }
    	String resultArray[] = sourceText.split("\n");
    	if(resultArray.length==1){
    		resultArray = sourceText.split("\r");
    	}
    	textArea2.clear();
    	
    	 for(int i =0 ; i< resultArray.length;i++){  
    		 sBuff.append("'");
    		 sBuff.append(resultArray[i]);
    		 if(i!=(resultArray.length-1)){
    		 sBuff.append("',\n");		
    		 }else{
    		 sBuff.append("'");
    		 }
    	 }
    	textArea2.setText(sBuff.toString());
    	sBuff.delete(0, sBuff.length());
   // 	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统剪切板       
		//StringSelection selection = new StringSelection(textArea2.getText());//构建String数据类型  
		//clipboard.setContents(selection, selection);//添加文本到系统剪切板     	 
     }
     
     protected static String getClipboardText() {
         Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统剪贴板
         // 获取剪切板中的内容
         Transferable clipT = clip.getContents(null);
         if (clipT != null) {
         // 检查内容是否是文本类型
         if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor))
			try {
				return (String)clipT.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
         }
         return null;
     }
	
     @SuppressWarnings("restriction")
	public void genOracleText(String sourceText){
       	 if(sourceText.trim()==""){
    		 return;
    	 }
    	String resultArray[] = sourceText.split("\n");
    	if(resultArray.length==1){
    		resultArray = sourceText.split("\r");
    	}
    	if(resultArray.length > maxRow){
    	Optional<ButtonType> buttonType = dd.showAndWait();
			if(buttonType.isPresent()){
				columnName = textField2.getText();
			}
    	}
    	textArea2.clear();   	
    	 for(int i =0 ; i< resultArray.length;i++){  
    		 if(i==0){
    			 sBuff.append(columnName+" in (\n'");    			 
    		 }else{
    		 sBuff.append("'");
    		 }
    		 sBuff.append(resultArray[i]);
    		 if((i%maxRow==0&&i!=0)){
    			 sBuff.append("'");
    			 if(i==(resultArray.length-1)){
    				 sBuff.append("\n)");
    			 }
        		 }else if(i==(resultArray.length-1)){
        			 sBuff.append("'\n)");
        		 }else{       		 
        		 sBuff.append("',\n");	
        		 }
    		 
    		 
    		 if(i!=0&&i%maxRow==0&&i!=(resultArray.length-1)){
    			 sBuff.append("\n) or "+columnName+" in (\n");    			 
    		 }
    		 
    	 }
    	textArea2.setText(sBuff.toString());
    	sBuff.delete(0, sBuff.length());    
    	columnName ="";
     }
     
}
