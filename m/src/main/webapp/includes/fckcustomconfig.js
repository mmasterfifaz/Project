FCKConfig.AutoDetectLanguage = false ;
FCKConfig.DefaultLanguage = "en" ;
FCKConfig.ToolbarSets["Standard"] = [
	['FontName','FontSize'],
	['TextColor','BGColor'],

        ['Cut','Copy','Paste','PasteText','PasteWord'],
	['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        '/',
	['Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['Link','Unlink','Anchor'],
	['Image','Table','Rule'], ['ShowBlocks','-','About']
			// No comma for the last row.
] ;
FCKConfig.FontNames = 'Arial;Helvetica;sans-serif;Tahoma' ;