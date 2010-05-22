/*
 * 04/23/2010
 *
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This code is licensed under the LGPL.  See the "license.txt" file included
 * with this project.
 */
package org.fife.rsta.ac;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * A base class for language support implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractLanguageSupport implements LanguageSupport {

	/**
	 * Map of all text areas using this language support to their installed
	 * {@link AutoCompletion} instances.  This should be maintained by
	 * subclasses by adding to, and removing from, it in their
	 * {@link #install(org.fife.ui.rsyntaxtextarea.RSyntaxTextArea)} and
	 * {@link #uninstall(org.fife.ui.rsyntaxtextarea.RSyntaxTextArea)} methods.
	 */
	private Map textAreaToAutoCompletion;

	/**
	 * Whether auto-completion is enabled for this language.
	 */
	private boolean autoCompleteEnabled;

	/**
	 * Whether parameter assistance should be enabled for this language.
	 */
	private boolean parameterAssistanceEnabled;

	/**
	 * Whether the description window is displayed when the completion list
	 * window is displayed.
	 */
	private boolean showDescWindow;

	/**
	 * The default renderer for the completion list.
	 */
	private ListCellRenderer renderer;


	/**
	 * Constructor.
	 */
	protected AbstractLanguageSupport() {
		setDefaultCompletionCellRenderer(null); // Force default
		textAreaToAutoCompletion = new HashMap();
		autoCompleteEnabled = true;
	}


	/**
	 * Creates the default cell renderer to use when none is specified.
	 * Subclasses can override this method if there is a "better" default
	 * renderer for a specific language.
	 *
	 * @return The default renderer for the completion list.
	 */
	protected ListCellRenderer createDefaultCompletionCellRenderer() {
		return new DefaultListCellRenderer();
	}


	/**
	 * Returns the auto completion instance used by a text area.
	 *
	 * @param textArea The text area.
	 * @return The auto completion instance, or <code>null</code> if none
	 *         is installed on the text area.
	 */
	protected AutoCompletion getAutoCompletionFor(RSyntaxTextArea textArea) {
		return (AutoCompletion)textAreaToAutoCompletion.get(textArea);
	}


	/**
	 * {@inheritDoc}
	 */
	public ListCellRenderer getDefaultCompletionCellRenderer() {
		return renderer;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean getShowDescWindow() {
		return showDescWindow;
	}


	/**
	 * Returns the text areas with this language support currently installed.
	 *
	 * @return The text areas.
	 */
	protected Set getTextAreas() {
		return textAreaToAutoCompletion.keySet();
	}


	/**
	 * Registers an auto-completion instance.  This should be called by
	 * subclasses in their
	 * {@link #install(org.fife.ui.rsyntaxtextarea.RSyntaxTextArea)} methods
	 * so that this language support can update all of them at once.
	 *
	 * @param textArea The text area that just installed the auto completion.
	 * @param ac The auto completion instance.
	 * @see #uninstallImpl(RSyntaxTextArea)
	 */
	protected void installImpl(RSyntaxTextArea textArea, AutoCompletion ac) {
		textAreaToAutoCompletion.put(textArea, ac);
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoCompleteEnabled() {
		return autoCompleteEnabled;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isParameterAssistanceEnabled() {
		return parameterAssistanceEnabled;
	}


	/**
	 * {@inheritDoc}
	 */
	public void setAutoCompleteEnabled(boolean enabled) {
		if (enabled!=autoCompleteEnabled) {
			autoCompleteEnabled = enabled;
			Iterator i=textAreaToAutoCompletion.values().iterator();
			while (i.hasNext()) {
				AutoCompletion ac = (AutoCompletion)i.next();
				ac.setAutoCompleteEnabled(enabled);
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void setDefaultCompletionCellRenderer(ListCellRenderer r) {
		if (r==null) {
			r = createDefaultCompletionCellRenderer();
		}
		renderer = r;
	}


	/**
	 * {@inheritDoc}
	 */
	public void setParameterAssistanceEnabled(boolean enabled) {
		if (enabled!=parameterAssistanceEnabled) {
			parameterAssistanceEnabled = enabled;
			Iterator i=textAreaToAutoCompletion.values().iterator();
			while (i.hasNext()) {
				AutoCompletion ac = (AutoCompletion)i.next();
				ac.setParameterAssistanceEnabled(enabled);
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void setShowDescWindow(boolean show) {
		if (show!=showDescWindow) {
			showDescWindow = show;
			Iterator i=textAreaToAutoCompletion.values().iterator();
			while (i.hasNext()) {
				AutoCompletion ac = (AutoCompletion)i.next();
				ac.setShowDescWindow(show);
			}
		}
	}


	/**
	 * Unregisters an textArea.  This should be called by subclasses in their
	 * {@link #uninstall(org.fife.ui.rsyntaxtextarea.RSyntaxTextArea)} methods.
	 * This method will also call the <code>uninstall</code> method on the
	 * <code>AutoComplete</code>.
	 *
	 * @param textArea The text area.
	 * @see #installImpl(RSyntaxTextArea, AutoCompletion)
	 */
	protected void uninstallImpl(RSyntaxTextArea textArea) {
		AutoCompletion ac = getAutoCompletionFor(textArea);
		if (ac!=null) {
			ac.uninstall();
		}
		textAreaToAutoCompletion.remove(textArea);
	}


}