package junitview.views;

//import org.eduride.junitview.tests.*;

import junitview.controller.ResultSorter;
import junitview.controller.ViewContentProvider;
import junitview.controller.ViewLabelProvider;
import junitview.model.TestList;
import junitview.model.TestResult;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.*;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class EduRideJunitView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "plugin.views.EduRideJunitView";

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	private TableColumn nameColumn;
	private TableColumn successColumn;
	private TableColumn messageColumn;
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	/**
	 * The constructor.
	 */
	public EduRideJunitView() {
	}


	private PageBook pagebook;
	private TableViewer tableviewer;
	private TextViewer textviewer;
	private Label label;
	
	// the listener we register with the selection service 
	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			// we ignore our own selections
			if (sourcepart != EduRideJunitView.this) {
			    showSelection(sourcepart, selection);
			}
		}
	};
	
	/**
	 * Shows the given selection in this view.
	 */
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		setContentDescription(sourcepart.getTitle() + " (" + selection.getClass().getName() + ")");
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			showItems(ss.toArray());
		}
		if (selection instanceof ITextSelection) {
			ITextSelection ts  = (ITextSelection) selection;
			showText(ts.getText());
		}
		if (selection instanceof IMarkSelection) {
			IMarkSelection ms = (IMarkSelection) selection;
			try {
			    showText(ms.getDocument().get(ms.getOffset(), ms.getLength()));
			} catch (BadLocationException ble) { }
		}
	}
	
	private void showItems(Object[] items) {
		tableviewer.setInput(items);
		pagebook.showPage(tableviewer.getControl());
	}
	
	private void showText(String text) {
		textviewer.setDocument(new Document(text));
		pagebook.showPage(textviewer.getControl());
	}

	public void dispose() {
		// important: We need do unregister our listener when the view is disposed
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}
		
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		//viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		
		//createColumns(parent);
		
		//final Table table = viewer.getTable();
		//table.setHeaderVisible(true);
		//table.setLinesVisible(true);
		/*
		nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setText("Name");
		nameColumn.setWidth(200);
		successColumn = new TableColumn(table, SWT.LEFT);
		successColumn.setText("Success");
		successColumn.setWidth(100);
		messageColumn = new TableColumn(table, SWT.LEFT);
		messageColumn.setText("Message");
		messageColumn.setWidth(200);
		//TableItem item = new TableItem(table, SWT.NONE);
	    //item.setText(new String[] { "entire", "row", "red foreground" });
	    //item.setForeground(red);
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		
		
		//viewer.setContentProvider(new ViewContentProvider(this));
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider(this));
		viewer.setSorter(new ResultSorter(this));
		//viewer.setInput(getViewSite());
		*/
		
		GridLayout layout = new GridLayout(2, false);
	    parent.setLayout(layout);
	    //Label searchLabel = new Label(parent, SWT.NONE);
	    //searchLabel.setText("Search: ");
	    //final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
	    //searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
	    //    | GridData.HORIZONTAL_ALIGN_FILL));
	    createViewer(parent);
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "plugin.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();		
		
		// the PageBook allows simple switching between two viewers
		pagebook = new PageBook(parent, SWT.NONE);
		
		tableviewer = new TableViewer(pagebook, SWT.NONE);
		tableviewer.setLabelProvider(new WorkbenchLabelProvider());
		tableviewer.setContentProvider(new ArrayContentProvider());
		
		// we're cooperative and also provide our selection
		// at least for the tableviewer
		getSite().setSelectionProvider(tableviewer);
		
		textviewer = new TextViewer(pagebook, SWT.H_SCROLL | SWT.V_SCROLL);
		textviewer.setEditable(false);
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
		
	}

	private void createViewer(Composite parent) {
	    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
	        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	    createColumns(parent, viewer);
	    final Table table = viewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);

	    viewer.setContentProvider(new ArrayContentProvider());
	    // Get the content for the viewer, setInput will call getElements in the
	    // contentProvider
	    Class<?> c = junitview.tests.SquareTest.class;
	    TestList temp = new TestList(c);
	    viewer.setInput(temp.getTestList());
	    // Make the selection available to other views
	    getSite().setSelectionProvider(viewer);
	    // Set the sorter for the table

	    // Layout the viewer
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    gridData.horizontalAlignment = GridData.FILL;
	    viewer.getControl().setLayoutData(gridData);
	  }

	  public TableViewer getViewer() {
	    return viewer;
	  }
	/*
	private void createColumns(final Composite parent, final TableViewer viewer) {
	    String[] titles = { "First name", "Last name", "Gender", "Married" };
	    int[] bounds = { 100, 100, 100, 100 };

	    // First column is for the first name
	    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
	    col.setLabelProvider(new CellLabelProvider() {
	      @Override
	      public void update(ViewerCell cell) {
	        cell.setText(((Person) cell.getElement()).getFirstName());
	      }
	    });
	    col.setEditingSupport(new FirstNameEditingSupport(viewer));

	    // Second column is for the last name
	    col = createTableViewerColumn(titles[1], bounds[1], 1);
	    col.setLabelProvider(new CellLabelProvider() {
	      @Override
	      public void update(ViewerCell cell) {
	        cell.setText(((Person) cell.getElement()).getLastName());
	      }
	    });
	    col.setEditingSupport(new LastNameEditingSupport(viewer));

	    // Now the gender
	    col = createTableViewerColumn(titles[2], bounds[2], 2);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        Person p = (Person) element;
	        return p.getGender();
	      }
	    });
	    col.setEditingSupport(new GenderEditingSupport(viewer));

	    // // Now the status married
	    col = createTableViewerColumn(titles[3], bounds[3], 3);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        return null;
	      }

	      @Override
	      public Image getImage(Object element) {
	        if (((Person) element).isMarried()) {
	          return CHECKED;
	        } else {
	          return UNCHECKED;
	        }
	      }
	    });
	    col.setEditingSupport(new MarriedEditingSupport(viewer));

	  }*/
	  private void createColumns(final Composite parent, final TableViewer viewer) {
		    String[] titles = { "Name", "Description" };
		    int[] bounds = { 100, 100 };

		    // First column is for the first name
		    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        TestResult t = (TestResult) element;
		        return t.getName();
		      }
		    });

		    // Second column is for the last name
		    col = createTableViewerColumn(titles[1], bounds[1], 1);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        TestResult p = (TestResult) element;
		        return p.getDescription();
		      }
		    });

		    /*
		    // // Now the status married
		    col = createTableViewerColumn(titles[3], bounds[3], 3);
		    col.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        return null;
		      }
		      
		      @Override
		      public Image getImage(Object element) {
		        if (((TestResult) element).isMarried()) {
		          return CHECKED;
		        } else {
		          return UNCHECKED;
		        }
		      }
		    });*/

		  }
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
	    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
	        SWT.NONE);
	    final TableColumn column = viewerColumn.getColumn();
	    column.setText(title);
	    column.setWidth(bound);
	    column.setResizable(true);
	    column.setMoveable(true);
	    return viewerColumn;
	  }

	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				EduRideJunitView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		// eventually we'll want a icon here that toggles whether the view changes when the editor changes
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Test Plugin",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
		pagebook.setFocus();
	}
}