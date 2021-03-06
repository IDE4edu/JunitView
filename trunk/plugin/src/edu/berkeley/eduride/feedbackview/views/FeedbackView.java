package edu.berkeley.eduride.feedbackview.views;

//import org.eduride.junitview.tests.*;
/*
import studentview.controller.NavigationListener;
import studentview.NavigatorActivator;
import studentview.model.Step;
*/
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.resource.ImageDescriptor;
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

import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.feedbackview.EduRideFeedback;
import edu.berkeley.eduride.feedbackview.controller.FeedbackController;
import edu.berkeley.eduride.feedbackview.model.*;

public class FeedbackView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "plugin.views.FeedbackView";

	
	private FeedbackController controller;
	public void setController(FeedbackController c) {
		controller = c;
	}
	

	private final Device device = Display.getCurrent();
	private Color white = new Color(device, 255, 255, 255);
	private Color gray = new Color(device, 190, 190, 190);
	private Color black = new Color(device, 0, 0, 0);
	private Color green = new Color(device, 0, 100, 0);
	private Color red = new Color(device, 255, 0, 0);
	


	
	/**
	 * The constructor.
	 */
	public FeedbackView() {
		super();
	}
	
	
	
	private Composite viewParent;
	private TableViewer viewer;

//	private PageBook pagebook;
//	private TableViewer tableviewer;
//	private TextViewer textviewer;

	// buttons at the upper right of the view
	private ActionToggle updateContinuously;
	private ActionToggle followEditorFocus;
	private Action updateNow;

	
	
	/*
	 * ---------------------------------------------------
	 * Create the view
	 */
	public void createPartControl(Composite parent) {
		
		controller = EduRideFeedback.getController();
		
		viewParent = parent;
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		
		createViewer(parent);
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "plugin.viewer");
		makeActions();
		hookContextMenu();
		//hookDoubleClickAction();
		contributeToActionBars();

		// the PageBook allows simple switching between two viewers
//		pagebook = new PageBook(parent, SWT.NONE);
//
//		tableviewer = new TableViewer(pagebook, SWT.NONE);
//		tableviewer.setLabelProvider(new WorkbenchLabelProvider());
//		tableviewer.setContentProvider(new ArrayContentProvider());

		// we're cooperative and also provide our selection
		// at least for the tableviewer
//		getSite().setSelectionProvider(tableviewer);

//		textviewer = new TextViewer(pagebook, SWT.H_SCROLL | SWT.V_SCROLL);
//		textviewer.setEditable(false);

		
//		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().layout(true, true);

	
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
		
		// TODO really?  compiles?  
		//refresh(EduRideFeedback.getController().getCurrentModel(), true);
	}

	
//	public TableViewer getViewer() {
//		return viewer;
//	}

	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Success", "Name", "Message", "Expected",
				"Observed" };
		int[] bounds = { 80, 120, 200, 100, 100 };
		//Console.msg("feedbackview creatingColumns");
		TableViewerColumn col;
		
		// First column is the name
		col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TestResult t = (TestResult) element;
				if (t.getSuccess()) {
					return "Correct";
				} else {
					return "Incorrect";
				}
			}

			public Color getBackground(Object element) {
				if (((TestResult) element).getSuccess()) {
					return green;
				} else {
					return red;
				}
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TestResult t = (TestResult) element;
				return t.getName();
			}
		});
		/*********** DESCRIPTION IS GOING TO BE A TOOLTIP */

		// Second column is the description
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TestResult t = (TestResult) element;
				return t.getMessage();
			}
		});
		/*******/
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TestResult t = (TestResult) element;
				return t.getExpected();
			}
		});

		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TestResult t = (TestResult) element;
				return t.getObserved();
			}
		});
		/***********/
		// TODO grey out the titles
	}

	
	
	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	
	
	
	
	
	/*
	 * ---------------------------
	 * Selection stuff
	 */
	
//	// the listener we register with the selection service
//	private ISelectionListener listener = new ISelectionListener() {
//		public void selectionChanged(IWorkbenchPart sourcepart,
//				ISelection selection) {
//			// we ignore our own selections
//			if (sourcepart != FeedbackView.this) {
//				showSelection(sourcepart, selection);
//			}
//		}
//	};
//
//	/**
//	 * Shows the given selection in this view.
//	 */
//	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
//		// setContentDescription(sourcepart.getTitle() + " (" +
//		// selection.getClass().getName() + ")");
//		if (selection instanceof IStructuredSelection) {
//			IStructuredSelection ss = (IStructuredSelection) selection;
//			showItems(ss.toArray());
//		}
//		if (selection instanceof ITextSelection) {
//			ITextSelection ts = (ITextSelection) selection;
//			showText(ts.getText());
//		}
//		if (selection instanceof IMarkSelection) {
//			IMarkSelection ms = (IMarkSelection) selection;
//			try {
//				showText(ms.getDocument().get(ms.getOffset(), ms.getLength()));
//			} catch (BadLocationException ble) {
//			}
//		}
//	}
//
//	private void showItems(Object[] items) {
//		tableviewer.setInput(items);
//		pagebook.showPage(tableviewer.getControl());
//	}
//
//	private void showText(String text) {
//		textviewer.setDocument(new Document(text));
//		pagebook.showPage(textviewer.getControl());
//	}
//
//	public void dispose() {
//		// important: We need do unregister our listener when the view is
//		// disposed
//		getSite().getWorkbenchWindow().getSelectionService()
//				.removeSelectionListener(listener);
//		super.dispose();
//	}

	
	

	
	////////////////////////////////
	// refreshing the view 
	
	
	public void refresh(IJUnitFeedbackModel model, JUnitFeedbackResults results, boolean compiles) {
		if (model == null) {
			// lets do nothing in the case, mkay?
			refreshFinished(model);
			return;
		}
		
		// Not compiling indicated by compiles==false, but also results==null;
		String title = model.getTitle();

		Table table = viewer.getTable();
		

    	if (compiles){
			table.setBackground(white);
			table.setForeground(black);
			viewer.setInput(results.getResults());
		} else {
			// Did not compile
			title = "(Compilation Errors) " + title;
			table.setBackground(gray);
			table.setForeground(gray);
			viewer.setInput(null);
		}
    	setContentDescription(title);
		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// Set the sorter for the table
		//TODO: perhaps the @IGNORE magic happens here?
		viewer.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				TestResult t = (TestResult) element;
				return (!t.getSuccess() || !t.hideWhenSuccessful());
			}
		});
		
		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
		
		//try to update the layout
		viewParent.layout();
		refreshFinished(model);
		
		//DEBUG
		Console.msg("Feedback View asked to refresh model of '" + model.getTestClass().getElementName() + "' with compiles: " + compiles);

	}
	
	
	private void refreshFinished(IJUnitFeedbackModel model) {
		EduRideFeedback.getController().refreshFinishedCallback(model);
	}
	

	
	////////////////////////////
	

	
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
//		pagebook.setFocus();
	}
	
	
	
	
	
	
	
	////////// toolbar, menus
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				FeedbackView.this.fillContextMenu(manager);
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
		manager.add(updateNow);
		manager.add(new Separator());
		manager.add(updateContinuously);
		manager.add(followEditorFocus);

	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(updateNow);
		manager.add(new Separator());
		manager.add(followEditorFocus);
		manager.add(updateContinuously);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(updateNow);
		manager.add(new Separator());
		manager.add(followEditorFocus);
		manager.add(updateContinuously);
	}

	
	
	// cannot be anonymous, oh the grief
	private class ActionToggle extends Action {
		public ImageDescriptor onImage = null;
		public ImageDescriptor offImage = null;
		public void setOnImage(ImageDescriptor img) {
			onImage = img;
		}
		public void setOffImage(ImageDescriptor img) {
			offImage = img;
		}

		public String onToolTip = "";
		public String offToolTip = "";
		public void setOnToolTip(String tooltip) {
			this.onToolTip = tooltip;
		}
		public void setOffToolTip(String tooltip) {
			this.offToolTip = tooltip;
		}

		public void setImageAndTooltip(boolean state) {
			if (state) {
				this.setImageDescriptor(onImage);
				this.setToolTipText(onToolTip);
			} else {
				this.setImageDescriptor(offImage);
				this.setToolTipText(offToolTip);
			}
		}
	}
	
	private void makeActions() {
		
		updateContinuously = new ActionToggle() {
			public void run() {
				// update continuously
				boolean state = controller.getUpdateContinuously();
				controller.setUpdateContinuously(!state);
				setImageAndTooltip(!state);
			}
		};
		updateContinuously.setText("Update Continuously");
		updateContinuously.setOnToolTip("Click to turn off continuous updating (update on save only)");
		updateContinuously.setOffToolTip("Click to turn on continuous updating (update after every keystroke)");
		updateContinuously.setOnImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		updateContinuously.setOffImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE_DISABLED));
		updateContinuously.setImageAndTooltip(controller.getUpdateContinuously());
		
		
		followEditorFocus = new ActionToggle() {
			public void run() {
				boolean state = controller.getFollowOnEditorFocus();
				controller.setFollowOnEditorFocus(!state);
				setImageAndTooltip(!state);	
			}
		};
		followEditorFocus.setText("Follow Editor Focus");
		followEditorFocus.setOnToolTip("Click to keep current tests even when changing editor focus");
		followEditorFocus.setOffToolTip("Click to change which tests to run whenever changing editor focus");
		followEditorFocus.setOnImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
		followEditorFocus.setOffImage(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED_DISABLED));
		followEditorFocus.setImageAndTooltip(controller.getFollowOnEditorFocus());
		
		
		updateNow = new Action() {
			//IMG_TOOL_REDO
			public void run() {
				controller.update();
				
				// should we force update, or choose updateNoCompile as appropriate??
			}
		};
		updateNow.setText("Update Now");
		updateNow.setToolTipText("Update Now (will save editor if necessary)");
		updateNow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));

	}

/*
  	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				updateNow.run();
			}
		});
	}
*/

	
	

}