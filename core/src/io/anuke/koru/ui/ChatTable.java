package io.anuke.koru.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.VisTextFieldStyle;

import io.anuke.koru.modules.Network;
import io.anuke.koru.modules.Renderer;
import io.anuke.koru.network.packets.ChatPacket;

public class ChatTable extends VisTable{
	private final static int messagesShown = 10;
	private Array<ChatMessage> messages = new Array<ChatMessage>();
	private boolean chatOpen = false;
	private VisTextField chatfield;
	private VisLabel fieldlabel = new VisLabel(">");
	private BitmapFont font;
	private float offsetx = 4, offsety = 4;
	
	
	public ChatTable(){
		super();
		font = VisUI.getSkin().getFont("pixel-font");
		font.getData().markupEnabled = true;
		addListener(input);
		
		chatfield = new VisTextField("", new VisTextField.VisTextFieldStyle(VisUI.getSkin().get(VisTextFieldStyle.class)));
		chatfield.getStyle().background = chatfield.getStyle().focusBorder;
		chatfield.getStyle().backgroundOver = null;
		chatfield.getStyle().font = VisUI.getSkin().getFont("smooth-font");
		bottom().left().padBottom(offsety).padLeft(offsetx).add(fieldlabel);
		
		add(chatfield).padBottom(offsety).padLeft(offsetx).growX().padRight(offsetx);
		
		
		//addMessage("some random text", 0, "ya boi");
		//addMessage("wew lad", 0, "dat boi");
		//addMessage("test", 0, "ye boi");
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		super.draw(batch, alpha);
		
		float spacing = font.getLineHeight();
		
		chatfield.setVisible(chatOpen);
		fieldlabel.setVisible(chatOpen);
		//if(chatOpen){
		//	font.draw(batch, "> enter some text or something", offsetx, offsety + spacing);
		//}
		
		for(int i = 0; i < messagesShown && i < messages.size; i ++){
			font.draw(batch, messages.get(i).formattedMessage, offsetx, offsety + spacing + (i+1)*spacing);
		}
	}
	
	private void sendMessage(){
		String message = chatfield.getText();
		chatfield.clearText();
		
		if(message.replaceAll(" ", "").isEmpty()) return;
		
		ChatPacket packet = new ChatPacket();
		packet.message = message;
		Renderer.i.getModule(Network.class).client.sendTCP(packet);
	}
	
	public void enterPressed(){
		if(!chatOpen && (getStage().getKeyboardFocus() == null || !getStage().getKeyboardFocus().getParent().isVisible())){
			FocusManager.switchFocus(chatfield.getStage(), chatfield);
			chatfield.getStage().setKeyboardFocus(chatfield);
			chatOpen = !chatOpen;
		}else if(chatOpen){
			getStage().setKeyboardFocus(null);
			chatOpen = !chatOpen;
			sendMessage();
		}
	}
	
	public boolean chatOpen(){
		return chatOpen;
	}
	
	public void addMessage(String message, long senderid, String sender){
		messages.insert(0, new ChatMessage(message, senderid, sender));
	}
	
	InputListener input = new InputListener(){
		
	};
	
	@SuppressWarnings("unused")
	private static class ChatMessage{
		public final long id;
		public final String sender;
		public final String message;
		public final String formattedMessage;
		
		public ChatMessage(String message, long senderid, String sender){
			id = senderid;
			this.message = message;
			this.sender = sender;
			formattedMessage = "[ROYAL]["+sender+"]: [YELLOW]"+message;
		}
	}
}
