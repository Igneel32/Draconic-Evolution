package com.brandon3055.draconicevolution.common.items;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.lib.Strings;

public class DraconiumIngot extends TolkienItem {
	public DraconiumIngot() {
		this.setUnlocalizedName(Strings.draconiumIngotName);
		this.setCreativeTab(DraconicEvolution.getCreativeTab(2));
		ModItems.register(this);
	}

}