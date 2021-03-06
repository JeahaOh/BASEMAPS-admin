package fi.fta.utils.generation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fi.fta.utils.JsonUtils;
import fi.fta.utils.ResourceUtils;

public class SystemMessagesSource
{
	
	private String messageFilter[];
	
	public SystemMessagesSource()
	{}
	
	public void addMessageFilter(String messageFilter[])
	{
		this.messageFilter = messageFilter;
	}
	
	public String collect()
	{
		MessageObject mo = this.getMessageObject();
		return "var MESSAGES = {}; (function(){ MESSAGES = " +
			JsonUtils.toJson(mo) + " })();";
	}
	
	private MessageObject getMessageObject()
	{
		MessagesBundle mb = new MessagesBundle(messageFilter);
		mb.setBasename(ResourceUtils.MESSAGES_RESOURCE);
		MessageObject mo = new MessageObject();
	    mo.setEn(mb.getBundleContent(ResourceUtils.MESSAGES_RESOURCE, Locale.ENGLISH));
	    return mo;
	}
	
	
	public class MessageObject
	{
		
		private  Map<String, String> en = new HashMap<>();
		
		public MessageObject()
		{}
		
		public Map<String, String> getEn() {
		    return en;
		}
		
		public void setEn(Map<String, String> en) {
		    this.en = en;
		}
		
	}
	
}
