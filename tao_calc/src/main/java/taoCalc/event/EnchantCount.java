package taoCalc.event;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import taoCalc.CalcManager;
import taoCalc.Const;
import taoCalc.dto.CalcInfo;

public class EnchantCount extends MessageEvent {


	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {

		if (!event.getMessage().getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}
		
		String messageContent = event.getMessage().getContentRaw();

		if(!messageContent.contains("エンチャント完了")) {
			return;
		}
		
		String userId = event.getMessage().getReferencedMessage().getAuthor().getId();
		if (userId == null || userId.isEmpty()) {
			return;
		}
		
		CalcManager Calcmanager = CalcManager.getINSTANCE();
		CalcInfo calcInfo = Calcmanager.getUserId(userId);
		if (calcInfo == null) {
			calcInfo = new CalcInfo();
		}
		
		if(messageContent.contains(Const.ENCHANT_N)) {
			calcInfo.addEnchant_n();
		}else if(messageContent.contains(Const.ENCHANT_SR)) {
			calcInfo.addEnchant_sr();
		}else if(messageContent.contains(Const.ENCHANT_R)) {
			calcInfo.addEnchant_r();
		}else if(messageContent.contains(Const.ENCHANT_U)) {
			calcInfo.addEnchant_unknown();
		}else if(messageContent.contains(Const.ENCHANT_A)) {
			calcInfo.addEnchant_anijaaa();
		}else if(messageContent.contains(Const.ENCHANT_T)) {
			calcInfo.addEnchant_tsukishima();
		}else if(messageContent.contains(Const.ENCHANT_MMO)) {
			calcInfo.addEnchant_mmo();
		}else if(messageContent.contains(Const.ENCHANT_TAO)) {
			calcInfo.addEnchant_tao();
		}
		Calcmanager.setData(userId, calcInfo);
	}
}
