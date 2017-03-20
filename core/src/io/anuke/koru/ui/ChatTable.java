package io.anuke.koru.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.VisTextFieldStyle;

import io.anuke.koru.Koru;
import io.anuke.koru.modules.Network;
import io.anuke.koru.modules.Renderer;
import io.anuke.koru.network.packets.ChatPacket;

public class ChatTable extends VisTable{
	private final static int messagesShown = 10;
	private Array<ChatMessage> messages = new Array<ChatMessage>();
	private float fadetime;
	private float lastfadetime;
	private boolean chatOpen = false;
	private VisTextField chatfield;
	private VisLabel fieldlabel = new VisLabel(">");
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();
	private float offsetx = 4, offsety = 4, fontoffsetx = 2, chatspace = 50;
	private float textWidth = 600;
	private Color shadowColor = new Color(0,0,0,0.4f);
	private float textspacing = 10;
	
	
	public ChatTable(){
		super();
		font = VisUI.getSkin().getFont("pixel-font");
		font.getData().markupEnabled = true;
		addListener(input);
		fieldlabel.setStyle(new LabelStyle(fieldlabel.getStyle()));
		fieldlabel.getStyle().font = font;
		fieldlabel.setStyle(fieldlabel.getStyle());
		fieldlabel.setFontScale(2);
		chatfield = new VisTextField("", new VisTextField.VisTextFieldStyle(VisUI.getSkin().get(VisTextFieldStyle.class)));
		
		chatfield.getStyle().background = VisUI.getSkin().getDrawable("blank");
		chatfield.getStyle().fontColor = Color.WHITE;
		chatfield.getStyle().focusBorder = null;
		chatfield.getStyle().backgroundOver = null;
		chatfield.getStyle().font = VisUI.getSkin().getFont("pixel-font");
		bottom().left().padBottom(offsety).padLeft(offsetx*2).add(fieldlabel);
		
		add(chatfield).padBottom(offsety).padLeft(offsetx).growX().padRight(offsetx).height(28);
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.setColor(shadowColor);
		if(chatOpen)
			batch.draw(VisUI.getSkin().getRegion("white"), offsetx, chatfield.getY(), Gdx.graphics.getWidth()-offsetx*2, chatfield.getHeight()-1);
		
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
			
			batch.draw(VisUI.getSkin().getRegion("white"), offsetx, theight-layout.height+1-4, textWidth, layout.height+textspacing);
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
		if(!chatOpen && (getStage().getKeyboardFocus() == null || !getStage().getKeyboardFocus().getParent().isVisible())){
			FocusManager.switchFocus(chatfield.getStage(), chatfield);
			chatfield.getStage().setKeyboardFocus(chatfield);
			chatOpen = !chatOpen;
			lastfadetime = fadetime;
			fadetime = messagesShown+1;
		}else if(chatOpen){
			getStage().setKeyboardFocus(null);
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
	
	InputListener input = new InputListener(){
		
	};
	
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
