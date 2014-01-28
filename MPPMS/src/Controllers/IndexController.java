package Controllers;

import Application.AppObservable;
import Models.Asset;
import Models.Component;
import Models.Project;
import Models.Task;
import Models.User;
import Models.User.Role;
import Views.AssetDetailView;
import Views.ComponentDetailView;
import Views.ImportAssetsView;
import Views.ProjectDetailView;
import Views.IndexView;
import Views.ProjectOverviewView;
import Views.ProjectsHierarchyView;
import Views.TaskDetailView;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IndexController implements Observer {
    private final User currentUser;
    
    private final IndexView view = new IndexView();
    private ProjectDetailController projectDetailController;
    private TaskDetailController taskDetailController;
    private ComponentDetailController componentDetailController;
    private AssetDetailController assetDetailController;
    private ProjectOverviewController projectOverviewController;
    private ImportAssetsController importAssetsController;
    private ProjectsHierarchyController contentHierarchyController;
    
    public IndexController(User currentUser) {
        this.currentUser = currentUser;
    }
    
    public void launch() {
        populateTables();
        
        this.view.setWelcomeMessage("Welcome, " + currentUser.getName() + "!");
        
        // Controls are enabled if the current logged in user is a Project Manager
        this.view.setCreateProjectButtonEnabled(this.currentUser.getRole() == Role.ProjectManager);
        this.view.setCreateTaskButtonEnabled(this.currentUser.getRole() == Role.ProjectManager ||
                                                this.currentUser.getRole() == Role.ProjectCoordinator ||
                                                this.currentUser.getRole() == Role.QCTeamLeader);
        this.view.setContentHierarchyButtonEnabled(this.currentUser.getRole() == Role.ProjectManager);
        
        // IndexView controls events
        this.view.addNewProjectButtonActionListener(new NewProjectButtonActionListener());
        this.view.addNewTaskButtonActionListener(new NewTaskButtonActionListener());
        this.view.addNewComponentButtonActionListener(new NewComponentButtonActionListener());
        this.view.addNewAssetButtonActionListener(new NewAssetButtonActionListener());   
        this.view.addImportAssetsButtonActionListener(new ImportAssetsButtonActionListener());
        this.view.addUserMenuLogOutActionListener(new UserMenuLogOutActionListener());
        this.view.addApplicationMenuExitActionListener(new ApplicationMenuExitActionListener());        
        this.view.addProjectsTableListSelectionListener(new ProjectsTableListSelectionListener());
        this.view.addTasksTableListSelectionListener(new TasksTableListSelectionListener());
        this.view.addComponentsTableListSelectionListener(new ComponentsTableListSelectionListener());
        this.view.addAssetsTableListSelectionListener(new AssetsTableListSelectionListener());        
        this.view.addTabChangeListener(new TabChangeListener());
        this.view.addContentHierarchyButtonActionListener(new ContentHierarchyButtonActionListener());
        
        this.view.setVisible(true);
        
        AppObservable.getInstance().addObserver(this);
    }
    
    private void populateTables() {
        this.view.setProjectsTableData(Project.getProjectsForUser(currentUser));
        this.view.setTasksTableData(Task.getTasksForUser(currentUser));
        this.view.setComponentsTableData(Component.getAllComponents());
        this.view.setAssetsTableData(Asset.getAllAssets());
    }
    
    private void projectValueChanged() {
        if (view.getSelectedProject() == null) {
            view.setDetailViewPanel(null);
        }
        else {
            ProjectDetailView projectDetailView = new ProjectDetailView();
            view.setDetailViewPanel(projectDetailView);
            projectDetailView.addTaskEditButtonActionListener(new ProjectDetailTaskEditButtonActionListener());
            projectDetailView.addComponentEditButtonActionListener(new ProjectDetailComponentEditButtonActionListener());
            projectDetailView.addOverviewButtonActionListener(new ProjectDetailOverviewButtonActionListener());
            
            boolean canEdit = currentUser.getRole() == Role.ProjectManager;
            boolean canEditTasks = canEdit || currentUser.getRole() == Role.ProjectCoordinator || currentUser.getRole() == Role.QCTeamLeader;
            projectDetailController = new ProjectDetailController(projectDetailView, view.getSelectedProject(), canEdit, canEditTasks);
            projectDetailController.initialise();
        }
    }
    
    private void taskValueChanged() {
        if (view.getSelectedTask() == null) {
            view.setDetailViewPanel(null);
        }
        else {
            TaskDetailView taskDetailView = new TaskDetailView();
            view.setDetailViewPanel(taskDetailView);
            taskDetailView.addAssetEditButtonActionListener(new TaskDetailAssetEditButtonActionListener());
            
            taskDetailController = new TaskDetailController(taskDetailView, view.getSelectedTask(), currentUser);
            taskDetailController.initialise();
        }
    }
    
    private void componentValueChanged() {
        if (view.getSelectedComponent() == null) {
            view.setDetailViewPanel(null);
        }
        else {
            ComponentDetailView componentDetailView = new ComponentDetailView();
            view.setDetailViewPanel(componentDetailView);
            componentDetailView.addAssetEditButtonActionListener(new ComponentDetailAssetEditButtonActionListener());
            
            componentDetailController = new ComponentDetailController(componentDetailView, view.getSelectedComponent());
            componentDetailController.initialise();
        }
    }
    
    private void assetValueChanged() {
        if (view.getSelectedAsset() == null) {
            view.setDetailViewPanel(null);
        }
        else {
            AssetDetailView assetDetailView = new AssetDetailView();
            view.setDetailViewPanel(assetDetailView);
            assetDetailController = new AssetDetailController(assetDetailView, view.getSelectedAsset());
            assetDetailController.initialise();
        }
    }
    
    private void valueChanged() {
        switch (view.getSelectedTabName()) {
            case "Projects":
                projectValueChanged();
                break;
            case "Tasks":
                taskValueChanged();
                break;
            case "Components":
                componentValueChanged();
                break;
            case "Assets":
                assetValueChanged();
                break;
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        // Store any currently selected Models in the tables
        Project selectedProject = view.getSelectedProject();
        Task selectedTask = view.getSelectedTask();
        Component selectedComponent = view.getSelectedComponent();
        Asset selectedAsset = view.getSelectedAsset();
        
        // Repopulate the tables
        populateTables();
        
        // Select any models which were previously selected
        view.setSelectedProject(selectedProject);
        view.setSelectedTask(selectedTask);
        view.setSelectedComponent(selectedComponent);
        view.setSelectedAsset(selectedAsset);
        
        // Ensure that we remain looking at the same tab / detail view
        valueChanged();
    }
    
    class NewProjectButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {        
            ProjectDetailView detailView = new ProjectDetailView();
            detailView.addDiscardButtonActionListener(new DiscardNewProjectActionListener());
            
            projectDetailController = new ProjectDetailController(detailView, new Project());
            projectDetailController.initialise();
            
            view.clearProjectSelection();
            view.setDetailViewPanel(detailView);
        }
    }
    
    class NewTaskButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TaskDetailView detailView = new TaskDetailView();
            detailView.addDiscardButtonActionListener(new DiscardNewProjectActionListener());
            
            taskDetailController = new TaskDetailController(detailView, new Task(), currentUser);
            taskDetailController.initialise();
            
            view.clearTaskSelection();
            view.setDetailViewPanel(detailView);
        }
    }
    
    class NewComponentButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ComponentDetailView detailView = new ComponentDetailView();
            detailView.addDiscardButtonActionListener(new DiscardNewProjectActionListener());
            
            componentDetailController = new ComponentDetailController(detailView, new Component());
            componentDetailController.initialise();
            
            view.clearComponentSelection();
            view.setDetailViewPanel(detailView);            
        }
    }
    
    class NewAssetButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AssetDetailView detailView = new AssetDetailView();
            detailView.addDiscardButtonActionListener(new DiscardNewProjectActionListener());
            
            assetDetailController = new AssetDetailController(detailView, new Asset());
            assetDetailController.initialise();
            
            view.clearAssetSelection();
            view.setDetailViewPanel(detailView);  
        }
    }
    
    class ImportAssetsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImportAssetsView detailView = new ImportAssetsView();
            
            importAssetsController = new ImportAssetsController(detailView);
            importAssetsController.initialise();
            
            view.clearAssetSelection();
            view.setDetailViewPanel(detailView);
        }
    }
    
    class DiscardNewProjectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setDetailViewPanel(new JPanel());
        }
    }
    
    class ApplicationMenuExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    
    class UserMenuLogOutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getSource());
            view.dispose();
            for (Frame frame : Frame.getFrames()) {
                if (frame.getTitle().equals("MPPMS - Login")) {
                    frame.setState(Frame.NORMAL);
                }
            }
        }
    }
    
    class ProjectsTableListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            projectValueChanged();
        }
    }
    
    class TasksTableListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            taskValueChanged();
        }
    }
    
    class ComponentsTableListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            componentValueChanged();
        }
    }
    
    class AssetsTableListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            assetValueChanged();
        }
    }
    
    class TabChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent ce) {
            valueChanged();
        }        
    }
    
    class ProjectDetailTaskEditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setSelectedTask(projectDetailController.getSelectedTask());
            view.setSelectedTab("Tasks");
        }
    }
    
    class ProjectDetailComponentEditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setSelectedComponent(projectDetailController.getSelectedComponent());
            view.setSelectedTab("Components");
        }
    }
    
    class TaskDetailAssetEditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setSelectedAsset(taskDetailController.getSelectedAsset());
            view.setSelectedTab("Assets");
        }
    }
    
    class ComponentDetailAssetEditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setSelectedAsset(componentDetailController.getSelectedAsset());
            view.setSelectedTab("Assets");
        }
    }
    
    class ProjectDetailOverviewButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            projectOverviewController = new ProjectOverviewController(view.getSelectedProject(), new ProjectOverviewView());
            projectOverviewController.launch();
        }
    }

    class ContentHierarchyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ProjectsHierarchyView detailView = new ProjectsHierarchyView();
            
            contentHierarchyController = new ProjectsHierarchyController(detailView, currentUser);
            contentHierarchyController.initialise();  
        }
    }
}
