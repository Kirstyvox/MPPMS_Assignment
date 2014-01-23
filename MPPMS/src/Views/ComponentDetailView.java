package Views;

import java.awt.event.ActionListener;
import javax.swing.ListModel;

public class ComponentDetailView extends javax.swing.JPanel {

    public ComponentDetailView() {
        initComponents();
    }
    
    public void setEditMode(boolean editMode) {
        saveButton.setVisible(editMode);
        discardButton.setVisible(editMode);
        editButton.setVisible(!editMode);
        descriptionArea.setEnabled(editMode);
        assetChoiceButton.setEnabled(editMode);
    }
    
    public void setIdLabelText(String text) {
        this.lblID.setText(text);
    }
    
    public void setDescriptionText(String text) {
        this.descriptionArea.setText(text);
    }
    
    public String getDescription() {
        return this.descriptionArea.getText();
    }
    
    public void setAssets(Object[] assets) {
        this.listAssets.setListData(assets);
    }
    
    public Object[] getAssets() {
        ListModel model = listAssets.getModel();
        Object[] items = new Object[model.getSize()];

        for(int i = 0; i < model.getSize(); i++) {
             items[i] =  model.getElementAt(i);
        }
        
        return items;
    }
    
    public void addAssetChoiceActionListener(ActionListener listener) {
        this.assetChoiceButton.addActionListener(listener);
    }
    
    public void addEditButtonActionListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void addSaveButtonActionListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
    
    public void addDiscardButtonActionListener(ActionListener listener) {
        discardButton.addActionListener(listener);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblProjectDetails = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        listAssets = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        assetChoiceButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        discardButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        editAssetButton = new javax.swing.JButton();

        lblProjectDetails.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProjectDetails.setText("Component Details");

        lblID.setText("ID:");

        jLabel1.setText("Description");

        descriptionArea.setColumns(20);
        descriptionArea.setFont(descriptionArea.getFont());
        descriptionArea.setLineWrap(true);
        descriptionArea.setRows(3);
        descriptionArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(descriptionArea);

        listAssets.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listAssets);

        jLabel2.setText("Assets");

        assetChoiceButton.setText("Add / Remove");

        discardButton.setText("Discard changes");

        saveButton.setText("Save");

        editButton.setText("Edit");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(discardButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(discardButton)
                .addComponent(saveButton)
                .addComponent(editButton))
        );

        editAssetButton.setText("Edit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblProjectDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblID))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(assetChoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editAssetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProjectDetails)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(assetChoiceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editAssetButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton assetChoiceButton;
    private javax.swing.JTextArea descriptionArea;
    private javax.swing.JButton discardButton;
    private javax.swing.JButton editAssetButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblProjectDetails;
    private javax.swing.JList listAssets;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
