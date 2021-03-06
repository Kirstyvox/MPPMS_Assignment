package Controllers;

import Application.AppObservable;
import Models.Asset;
import Models.Component;
import Models.Project;
import Models.SetOfAssets;
import Models.SetOfComponents;
import Models.SetOfProjects;
import Models.SetOfTasks;
import Models.Task;
import Models.User;
import Views.ProjectsHierarchyView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Controller for ProjectsHierarchyView
 * @author Kirsty
 */

public class ProjectsHierarchyController implements Observer {
    private final ProjectsHierarchyView view;
    private final User currentUser;
    private Asset asset;
    private Project selectedProject;
    private DefaultMutableTreeNode selectedNode;
    
    /**
     * 
     * @param view The view for the controller
     * @param currentUser The current logged in user
     */
    public ProjectsHierarchyController(ProjectsHierarchyView view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
        
        this.view.addProjectsTreeSelectionListener(new ProjectsTreeSelectionListener());
        this.view.addAddToTaskButtonActionListener(new AddToTaskActionListener());
        this.view.addAddToComponentButtonActionListener(new AddToComponentActionListener());
        this.view.addRemoveFromTaskButtonActionListener(new RemoveFromTaskActionListener());
        this.view.addRemoveFromComponentButtonActionListener(new RemoveFromComponentActionListener());
    }
    
    /**
     *  Changes the visibility and enabled state of the controls to false 
     *  as an asset won't have been selected on initialisation
     * 
     *  Implements Observable
     */
    public void initialise() {
        view.setControlsEnabled(false);
        view.setControlsVisible(false);
        view.setVisible(true);
        refreshView(); // Populates the JTree (projectTree)

        // Observer pattern: update() is called when this is notified by AppObservable
        // Changes made to a model (e.g. Task) elsewhere will be reflected in the ProjectsHierarchyView
        AppObservable.getInstance().addObserver(this);
    }
    
    /**
     * Populates the JTree
     * 
     * Gets the projects for the current logged in user and iterates through it
     * Gets all the tasks and components for each project 
     * and generates nodes that are added to their project node as children
     * The relevant assets for these tasks and components are added as their children
     * 
     * A new TreeModel is generated from the rootNode and this new model is set as the JTree's model
     */
    private void refreshView() {
       // All the projects that the current logged in user has access to
       SetOfProjects projects = Project.getProjectsForUser(currentUser);
       
       DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Projects");
       
       if (projects != null) {
            for (Project project : projects) {
                // Create Project node
                DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project);
                
                // Get all tasks for project node
                SetOfTasks tasks = project.getTasks();
                DefaultMutableTreeNode taskRootNode = new DefaultMutableTreeNode("Tasks");
                
                if (tasks != null) {
                    for (Task task : tasks) {
                        DefaultMutableTreeNode taskNode = new DefaultMutableTreeNode(task);

                        // Get all assets for task node
                        SetOfAssets assets = task.getAssets();
                        if (assets != null) {
                            for (Asset taskAsset : assets) {
                                taskNode.add(new DefaultMutableTreeNode(taskAsset));
                            }
                        }
                        
                        taskRootNode.add(taskNode);
                    }
                    projectNode.add(taskRootNode);
                }
                
                DefaultMutableTreeNode comRootNode = new DefaultMutableTreeNode("Components");
                
                //Get all components for project node
                SetOfComponents coms = project.getComponents();
                if (coms != null) {
                    for (Component com : coms) {
                        DefaultMutableTreeNode comNode = new DefaultMutableTreeNode(com);
                        
                        //Get all assets for component node
                        SetOfAssets assets = com.getAssets();
                        if (assets != null) {
                            for (Asset compAsset : assets) {
                                comNode.add(new DefaultMutableTreeNode(compAsset));
                            }
                        }
                        comRootNode.add(comNode);
                    }
                    projectNode.add(comRootNode);
                }
                rootNode.add(projectNode);
            }

            DefaultTreeModel newModel = new DefaultTreeModel(rootNode);
            view.setTreeModel(newModel);
       }
    }
    
   /**
    * 
    * @param o
    * @param o1 
    */
    @Override
    public void update(Observable o, Object o1) {       
        refreshView();
        // Ensures that asset is up-to-date
           /* Asset.getAssetByID checks a static SetOfAssets
              which has just been repopulated from the database */
        this.asset = Asset.getAssetByID(this.asset.getId());
        
        // Populates the controls on the right of the split pane which the selected assets information
        updateAssetDetail();
    }
    
    /**
     * Populates the panel on the right of the scroll pane with the selected Asset details
     * 
     * Gets tasks that the assets is on within the project 
     * and the components that is is connected to within the project
     */
    public void updateAssetDetail() {
        
        // Labels showing what project and asset is currently selected
        view.setProjectDetails(selectedProject.toString());
        view.setAssetDetails(asset.toString());
        
        // Controls can be used as an asset has been selected
        view.setControlsEnabled(true);

        // Only retrieve the tasks and components for the selected project
        SetOfTasks tasks = selectedProject.getTasks();
        SetOfComponents comps = selectedProject.getComponents();

        SetOfTasks removeTasks = new SetOfTasks();
        SetOfTasks addTasks = new SetOfTasks();
        SetOfComponents removeComps = new SetOfComponents();
        SetOfComponents addComps = new SetOfComponents();


        // Populate Task ComboBoxes
        for (Task task : tasks) {
           SetOfAssets taskAssets = task.getAssets();
           if (taskAssets.contains(asset))
               removeTasks.add(task);
           else
               addTasks.add(task);
        }

        // Populate Component ComboBoxes
        for (Component comp : comps) {
           SetOfAssets compAssets = comp.getAssets();
           if (compAssets.contains(asset))
               removeComps.add(comp);
           else
               addComps.add(comp);
        }

        // Controls will only be enabled if there is a task(s)/component(s) to be displayed
        view.removeTasksEnabled(removeTasks.size() > 0);
        view.setRemoveTasksComboBox(removeTasks.toArray());

        view.addTasksEnabled(addTasks.size() > 0);
        view.setAddtoTasksComboBox(addTasks.toArray());

        view.removeComponentsEnabled(removeComps.size() > 0);
        view.setRemoveComponentsComboBox(removeComps.toArray());

        view.addComponentsEnabled(addComps.size() > 0);
        view.setAddtoComponentsComboBox(addComps.toArray());

        view.setControlsVisible(true);
    }
    
    
    /**
     * Triggered when a node is selected within the JTree
     * 
     * First checks to see if the node is an instance of Asset, 
     * if so then it will also check parent nodes to get the project that it relates to
     */
    class ProjectsTreeSelectionListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) { 
            // Get selected node object
            selectedNode = view.getSelectedTreeNode();
            if (selectedNode != null) {
                Object assetObj = selectedNode.getUserObject();
            
                // Check that the node selected can be cast to an Asset
                if (assetObj instanceof Asset) {
                    asset = (Asset) assetObj; 

                    // If an asset is selected then 3 levels up should be a project
                    DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent().getParent();
                    Object projectObj = projectNode.getUserObject();

                    /* The current hierarchy of models mean that projectObj shouldn't 
                            be anything other than a Project
                    - this check will prevent an error should the models change structure */
                    if (projectObj instanceof Project)
                    {
                        selectedProject = (Project) projectObj;
                        updateAssetDetail();
                    }
                } 
                else {
                    view.setControlsEnabled(false);
                    view.setControlsVisible(false);
                }     
            }
            
        }
    }
    
    /**
     * Adds the selected asset to the task selected
     */
    class AddToTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Task selectedTask = view.getSelectedAddTask();
            
            if (asset != null) {
                SetOfAssets tasksAssets = selectedTask.getAssets();
                tasksAssets.add(asset);
                selectedTask.setAssets(tasksAssets);
                selectedTask.save();
            }
            
        }
    }
    
    /**
     * Removes the asset selected from the task selected
     */
    class RemoveFromTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Task selectedTask = view.getSelectedRemoveTask();
            
            if (asset != null) {
                SetOfAssets tasksAssets = selectedTask.getAssets();
                tasksAssets.remove(asset);
                selectedTask.setAssets(tasksAssets);
                selectedTask.save();
            }
        }
    }
    
    /**
     * Adds the asset selected to the component selected
     */
    class AddToComponentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component selectedComponent = view.getSelectedAddComponent();
            
            if (asset != null) {
                SetOfAssets componentAssets = selectedComponent.getAssets();
                componentAssets.add(asset);
                selectedComponent.setAssets(componentAssets);
                selectedComponent.save();
            }
        }
    }
    
    /**
     * Removes the asset selected from the component selected
     */
    class RemoveFromComponentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component selectedComponent = view.getSelectedRemoveComponent();
            
            if (asset != null) {
                SetOfAssets componentAssets = selectedComponent.getAssets();
                componentAssets.remove(asset);
                selectedComponent.setAssets(componentAssets);
                selectedComponent.save();
            }
        }
    }
}
