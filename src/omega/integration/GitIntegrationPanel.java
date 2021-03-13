package omega.integration;
import omega.Screen;
import omega.utils.UIManager;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import java.io.File;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import omega.comp.RTextField;
import omega.comp.TextComp;
import java.awt.Font;
import javax.imageio.ImageIO;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static omega.utils.UIManager.*;
import static omega.settings.Screen.*;
public class GitIntegrationPanel extends JPanel {
	
	private BufferedImage gitImage;
	private TextComp initComp;
	private RTextField urlField;
	private String msg = "";
	private RTextField branchField;
	private RTextField commitMsgField;
	private TextComp commitComp;
	
	private TextComp pullComp;
	private JScrollPane messagePane;
	private JTextArea messageArea;
	private Git git;
     
	public GitIntegrationPanel(){
		super(null);
		setBackground(c2);
		setSize(500, 600);
		init();
	}
	
	public void init(){
		try{
               String imageName = UIManager.isDarkMode() ? "git-dark.png" : "git.png";
			gitImage = ImageIO.read(getClass().getResourceAsStream("/" + imageName));
		}
		catch(Exception e){
			System.err.println(e);
		}
		
		Component imageComp = new Component() {
			private String title = "Git Integration Panel";
			@Override
			public void paint(Graphics graphics){
				Graphics2D g = (Graphics2D)graphics;
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g.setColor(c2);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(gitImage, 0, 0, 80, 80, this);
				g.setFont(PX28);
				g.setColor(c3);
				g.drawString(title, getWidth()/2 - g.getFontMetrics().stringWidth(title)/2, getHeight()/2 - g.getFontMetrics().getHeight()/2 + g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent() + 1);
				g.fillRect(10, getHeight() - 4, getWidth() - 20, 4);
				g.setFont(PX16);
				String msg = GitIntegrationPanel.this.msg;
				if(msg.length() > 37){
					msg = msg.substring(0, 35) + "...";
				}
				g.drawString(msg, getWidth()/2 - g.getFontMetrics().stringWidth(msg)/2, getHeight() - 20);
			}
		};
		imageComp.setBounds(0, 0, getWidth(), 100);
		add(imageComp);
		initComp = new TextComp("init project as repository", c1, c2, c3, this::initRepository);
		initComp.setBounds(10, 120, getWidth() - 20, 40);
		initComp.setFont(PX18);
		add(initComp);
		
		urlField = new RTextField("type repository url", "https://github.com/", c1, c2, c3);
		urlField.setBounds(10, 180, getWidth() - 20, 40);
		urlField.setFont(PX16);
		add(urlField);
      
		branchField = new RTextField("type branch name, default : main", "main", c1, c2, c3);
		branchField.setBounds(10, 240, getWidth() - 20, 40);
		branchField.setFont(PX16);
		add(branchField);
        
		commitMsgField = new RTextField("type commit message, default : Update!", "Update!", c1, c2, c3);
		commitMsgField.setBounds(10, 300, getWidth() - 20, 40);
		commitMsgField.setFont(PX16);
		commitMsgField.setAlignmentX(RTextField.CENTER_ALIGNMENT);
		add(commitMsgField);
          
		commitComp = new TextComp("commit changes", c1, c2, c3, this::commitChanges);
		commitComp.setBounds(10, 360, getWidth() - 20, 40);
		commitComp.setFont(PX18);
		add(commitComp);
         
		pullComp = new TextComp("pull remote repository", c1, c2, c3, this::pullRepository);
		pullComp.setBounds(10, 420, getWidth() - 20, 40);
		pullComp.setFont(PX18);
		add(pullComp);
      
		messagePane = new JScrollPane(messageArea = new JTextArea());
		messagePane.setBounds(10, 470, getWidth() - 20, getHeight() - 480);
		messageArea.setFont(PX16);
		messageArea.setBackground(c2);
		messageArea.setForeground(c3);
		messagePane.setBackground(c2);
		messagePane.setForeground(c3);
		messageArea.setEditable(false);
		add(messagePane);
	}
	public void setRepository(String repoPath){
		try{
			git = Git.open(new File(repoPath));
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
	
	public void initRepository(){
		try{
			setMessage("Intializing Project as Repository...");
			//Initializing Repository
			Repository repository = new FileRepositoryBuilder().setGitDir(new File(Screen.getFileView().getProjectPath() + File.separator + ".git"))
			.readEnvironment() // scan environment GIT_* variables
			.findGitDir() // scan up the file system tree
			.setMustExist(true)
			.build();
			setMessage("");
			git = new Git(repository);
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
	public void commitChanges(){
		try{
			if(commitMsgField.getText().equals("")){
				setMessage("Please type in a commit message");
				return;
			}
			printMessage("Commiting Changes...");
			if(git == null){
				git = Git.open(new File(Screen.getFileView().getProjectPath()));
			}
			git.add().addFilepattern(".").call();
			git.commit().setAll(true).setMessage(commitMsgField.getText()).call();
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
	
	public void pullRepository(){
		if(!urlField.hasText()){
			setMessage("Please type in the Repository URL");
			return;
		}
		String url = urlField.getText();
		String repoName = url.endsWith(".git") ? url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')) : url.substring(url.lastIndexOf('/') + 1);
		setMessage("Pulling " + repoName + "...");
		//Pulling Repository
		setMessage("");
	}
    
	public void printMessage(String text){
		messageArea.append(messageArea.getText().equals("") ? text : "\n" + text);
	}
    
	public void setMessage(String msg){
		this.msg = msg;
		repaint();
	}

     public BufferedImage getImage(){
     	return gitImage;
     }
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Git-Integration");
		f.setUndecorated(true);
		f.setSize(500, 600);
		f.setLocationRelativeTo(null);
		f.add(new GitIntegrationPanel(), BorderLayout.CENTER);
		f.setVisible(true);
	}
}
