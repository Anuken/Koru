package io.anuke.koru.ui;


import static io.anuke.ucore.scene.style.Styles.styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.Network;
import io.anuke.koru.modules.Renderer;
import io.anuke.koru.network.packets.ChatPacket;
import io.anuke.ucore.scene.Scene;
import io.anuke.ucore.scene.style.Styles;
import io.anuke.ucore.scene.ui.Label;
import io.anuke.ucore.scene.ui.Label.LabelStyle;
import io.anuke.ucore.scene.ui.TextField;
import io.anuke.ucore.scene.ui.layout.Table;

public class ChatTable extends Table{
	private final static int messagesShown = 10;
	private Array<ChatMessage> messages = new Array<ChatMessage>();
	private float fadetime;
	private float lastfadetime;
	private boolean chatOpen = false;
	private TextField chatfield;
	private Label fieldlabel = new Label(">");
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();
	private float offsetx = 4, offsety = 4, fontoffsetx = 2, chatspace = 50;
	private float textWidth = 600;
	private Color shadowColor = new Color(0,0,0,0.4f);
	private float textspacing = 10;
	
	
	public ChatTable(){
		super();
		font = styles.getFont("pixel-font");
		font.getData().markupEnabled = true;
		fieldlabel.setStyle(new LabelStyle(fieldlabel.getStyle()));
		fieldlabel.getStyle().font = font;
		fieldlabel.setStyle(fieldlabel.getStyle());
		fieldlabel.setFontScale(2);
		chatfield = new TextField("", new TextField.TextFieldStyle(styles.get(TextField.TextFieldStyle.class)));
		
		chatfield.getStyle().background = styles.getDrawable("blank");
		chatfield.getStyle().fontColor = Color.WHITE;
		//chatfield.getStyle().focusBorder = null;
		//chatfield.getStyle().backgroundOver = null;
		chatfield.getStyle().font = Styles.styles.getFont("pixel-font");
		bottom().left().padBottom(offsety).padLeft(offsetx*2).add(fieldlabel);
		
		add(chatfield).padBottom(offsety).padLeft(offsetx).growX().padRight(offsetx).height(28);
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.setColor(shadowColor);
		if(chatOpen)
			batch.draw(styles.getRegion("white"), offsetx, chatfield.getY(), Gdx.graphics.getWidth()-offsetx*2, chatfield.getHeight()-1);
		
		font.getData().setScale(2f);
		font.getData().down = -21.5f;
		font.getData().lineHeight = 22f;
		
		super.draw(batch, alpha);
		
		
		float spacing = chatspace;
		
		chatfield.setVisible(chatOpen);
		fieldlabel.setVisible(chatOpen);
		
		batch.setColor(shadowColor);
		
		float theight = offsety + spacing;
		for(int i = 0; i < messagesShown && i < messages.size && i < fadetime; i ++){
			
			layout.setText(font, messages.get(i).formattedMessage, Color.WHITE, textWidth, Align.bottomLeft, true);
			theight += layout.height+textspacing;
			if(i == 0)theight -= textspacing+1;
			
			font.getCache().clear();
			font.getCache().addText(messages.get(i).formattedMessage, fontoffsetx + offsetx, offsety + theight, textWidth, Align.bottomLeft, true);
			
			if(fadetime-i < 1f && fadetime-i >= 0f){
				font.getCache().setAlphas(fadetime-i);
				batch.setColor(0,0,0,shadowColor.a*(fadetime-i));
					
			}
			
			batch.draw(styles.getRegion("white"), offsetx, theight-layout.height+1-4, textWidth, layout.height+textspacing);
			batch.setColor(shadowColor);
			
			font.getCache().draw(batch);
		}
		
		batch.setColor(Integer.MAX_VALUE);
		
		if(fadetime > 0 && !chatOpen)
		fadetime -= Gdx.graphics.getDeltaTime()*60f/120f;
	}
	
	private void sendMessage(){
		String message = chatfield.getText();
		chatfield.clearText();
		
		if(message.replaceAll(" ", "").isEmpty()) return;
		
		ChatPacket packet = new ChatPacket();
		packet.message = message;
		Koru.module(Renderer.class).getModule(Network.class).client.sendTCP(packet);
	}
	
	public void enterPressed(){
		Scene scene = getScene();
		
		if(!chatOpen && (scene.getKeyboardFocus() == null || !scene.getKeyboardFocus().getParent().isVisible())){
			//FocusManager.switchFocus(chatfield.getStage(), chatfield);
			scene.setKeyboardFocus(chatfield);
			chatOpen = !chatOpen;
			lastfadetime = fadetime;
			fadetime = messagesShown+1;
		}else if(chatOpen){
			scene.setKeyboardFocus(null);
			chatOpen = !chatOpen;
			sendMessage();
			fadetime = lastfadetime;
		}
	}
	
	public boolean chatOpen(){
		return chatOpen;
	}
	
	public void addMessage(String message, String sender){
		messages.insert(0, new ChatMessage(message, sender));
		
		fadetime += 1f;
		fadetime = Math.min(fadetime, messagesShown)+2f;
	}
	
	private static class ChatMessage{
		public final String sender;
		public final String message;
		public final String formattedMessage;
		
		public ChatMessage(String message, String sender){
			this.message = message;
			this.sender = sender;
			if(sender == null){ //no sender, this is a server message
				formattedMessage = message;
			}else{
				formattedMessage = "[ROYAL]["+sender+"]: [YELLOW]"+message;
			}
		}
	}
}
