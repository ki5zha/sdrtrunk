package io.github.dsheirer.gui.viewer;

import io.github.dsheirer.gui.SDRTrunk;
import javafx.css.CssMetaData;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import jiconfont.IconFont;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconCodeConverter;
import jiconfont.javafx.IconNode;

public class FontAwesomeUtil
{
	static {
		IconFontFX.register(FontAwesome.getIconFont());
	}
	
	public static FontAwesome getIcon(String iconName){
		return FontAwesome.valueOf(iconName.toUpperCase());
		
	}

}