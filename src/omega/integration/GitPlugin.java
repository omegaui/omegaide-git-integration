package omega.integration;
import omega.popup.OPopupItem;
import java.awt.Dimension;
import omega.comp.TextComp;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import omega.tabPane.IconManager;
import omega.utils.UIManager;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import omega.plugin.Plugin;

import static omega.utils.UIManager.*;
import static omega.settings.Screen.*;

public class GitPlugin implements Plugin{
	private BufferedImage image;
	private LinkedList<BufferedImage> screenShots;
	private JDialog gitIntegrationWindow;
     private GitIntegrationPanel gitIntegrationPanel;
     private OPopupItem gitItem;
	@Override
	public void init(){
          gitIntegrationPanel = new GitIntegrationPanel();
          
          image = gitIntegrationPanel.getImage();
          
		gitIntegrationWindow = new JDialog(getIDE());
          gitIntegrationWindow.setModal(true);
          gitIntegrationWindow.setTitle("Git Integration Panel");
          gitIntegrationWindow.setIconImage(image);
          gitIntegrationWindow.setUndecorated(true);
		gitIntegrationWindow.setSize(500, 630);
		gitIntegrationWindow.setLocationRelativeTo(null);
          gitIntegrationWindow.setResizable(false);
		gitIntegrationWindow.add(gitIntegrationPanel, BorderLayout.CENTER);

          TextComp closeComp = new TextComp("close", c1, c2, c3, ()->gitIntegrationWindow.setVisible(false));
          closeComp.setPreferredSize(new Dimension(500, 30));
          closeComp.setFont(PX16);
          closeComp.setArc(0, 0);
          gitIntegrationWindow.add(closeComp, BorderLayout.SOUTH);

          gitItem = new OPopupItem(getIDE().getToolMenu().toolsPopup, "Git Panel", image, ()->gitIntegrationWindow.setVisible(true));
	}
	
	@Override
	public void enable() {
		getIDE().getToolMenu().toolsPopup.addItem(gitItem);
	}
	@Override
	public void disable() {
		getIDE().getToolMenu().toolsPopup.removeItem("Git Panel");
	}
	@Override
	public BufferedImage getImage() {
		return image;
	}
	@Override
	public LinkedList<BufferedImage> getImages() {
		if(screenShots == null){
			try{
				screenShots = new LinkedList<>();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return screenShots;
	}
	@Override
	public String getName() {
		return "Minimal Git Integration";
	}
	@Override
	public String getVersion() {
		return "v1.8";
	}
	@Override
	public String getDescription() {
		return "This Release supports the following features : \n" +
		"Initializing a project as git repository.\n" +
		"Committing changes to the repository locally.\n" +
		"Pulling the Remote Repository.\n\n" +
		"Made using JGit java library.\n" +
		"More Features coming soon.";
	}
	@Override
	public String getAuthor() {
		return "Omega UI";
	}
	@Override
	public String getCopyright() {
		return "Copyright (C) 2021 Omega UI. All Right Reserved.";
	}
	
}
