package com.apogee.dev.CUJaS.UI;

import mdlaf.shadows.DropShadowBorder;
import mdlaf.themes.AbstractMaterialTheme;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;

/**
 * Reference: <a href="https://www.material-theme.com/docs/reference/color-palette">material-theme/docs/reference/color-palette</a>
 *
 * @see mdlaf.themes.MaterialLiteTheme
 * @author <a href="https://github.com/vincenzopalazzo">vincenzopalazzo</a>
 */
public class CUJASTheme extends AbstractMaterialTheme {

    @Override
    protected void installBorders() {
        super.installBorders();

        this.borderTable = MaterialBorders.LIGHT_LINE_BORDER;
        this.borderTableHeader =
                new BorderUIResource(
                        new DropShadowBorder(this.backgroundPrimary, 5, 3, 0.4f, 12, true, true, true, true));

        super.borderTitledBorder = MaterialBorders.LIGHT_LINE_BORDER;
    }

    @Override
    protected void installDefaultColor() {
        super.installDefaultColor();
        this.buttonDisabledForeground = this.disableTextColor;
    /* this.buttonDefaultTextColor = MaterialColors.WHITE;
    this.thumbDarkShadowColorScrollBar = MaterialColors.GRAY_500;
    this.thumbHighlightColorScrollBar = MaterialColors.GRAY_500;
    this.thumbShadowColorScrollBar = MaterialColors.GRAY_500;
    this.arrowButtonOnClickColorScrollBar = MaterialColors.GRAY_400;
    this.mouseHoverColorScrollBar = MaterialColors.GRAY_300;
    super.foregroundToolTip = MaterialColors.WHITE; */
        super.titleColorTaskPane = this.textColor;
        super.backgroundToolTip = this.disableTextColor;
    }

    @Override
    protected void installColor() {
        ColorUIResource secondBackground = new ColorUIResource(238, 238, 238);
        ColorUIResource disableBackground = new ColorUIResource(210, 212, 213);
        ColorUIResource accentColor = new ColorUIResource(231, 231, 232);
        ColorUIResource selectedForeground = new ColorUIResource(84, 110, 122);
        ColorUIResource selectedBackground = new ColorUIResource(220, 239, 237);
        this.backgroundPrimary = new ColorUIResource(240, 240, 240);
        this.highlightBackgroundPrimary = new ColorUIResource(0, 117, 212);

        this.textColor = new ColorUIResource(67, 67, 67);
        this.disableTextColor = new ColorUIResource(148, 167, 176);

        this.buttonBackgroundColor = new ColorUIResource(243, 244, 245);
        this.buttonBackgroundColorMouseHover = new ColorUIResource(231, 231, 232);
        this.buttonDefaultBackgroundColorMouseHover = this.buttonBackgroundColorMouseHover;
        this.buttonDefaultBackgroundColor = secondBackground;
        this.buttonDisabledBackground = disableBackground;
        this.buttonFocusColor = this.textColor;
        this.buttonDefaultFocusColor = this.buttonFocusColor;
        this.buttonBorderColor = new ColorUIResource(211, 225, 232);
        this.buttonColorHighlight = selectedBackground;

        this.selectedInDropDownBackgroundComboBox = this.buttonBackgroundColorMouseHover;
        this.selectedForegroundComboBox = this.textColor;

        this.menuBackground = this.backgroundPrimary;
        this.menuBackgroundMouseHover = this.buttonBackgroundColorMouseHover;

        this.arrowButtonColorScrollBar = this.buttonBackgroundColor;
        this.trackColorScrollBar = accentColor;
        this.thumbColorScrollBar = disableBackground;

        this.trackColorSlider = this.textColor;
        this.haloColorSlider = MaterialColors.bleach(this.highlightBackgroundPrimary, 0.5f);

        this.highlightColorTabbedPane = this.buttonColorHighlight;
        this.borderHighlightColorTabbedPane = this.buttonColorHighlight;
        this.focusColorLineTabbedPane = this.highlightBackgroundPrimary;
        this.disableColorTabTabbedPane = disableBackground;

        this.backgroundTable = this.backgroundPrimary;
        this.backgroundTableHeader = this.backgroundPrimary;
        this.selectionBackgroundTable = this.buttonBackgroundColorMouseHover;
        this.gridColorTable = this.backgroundPrimary;
        this.alternateRowBackgroundTable = this.backgroundPrimary;

        this.backgroundTextField = accentColor;
        this.inactiveForegroundTextField = this.textColor;
        this.inactiveBackgroundTextField = accentColor;
        this.selectionBackgroundTextField = selectedBackground;
        this.selectionForegroundTextField = selectedForeground;
        super.disabledBackgroudnTextField = disableBackground;
        super.disabledForegroundTextField = this.disableTextColor;
        this.inactiveColorLineTextField = this.textColor;
        this.activeColorLineTextField = this.highlightBackgroundPrimary;

        this.mouseHoverButtonColorSpinner = this.buttonBackgroundColorMouseHover;
        this.titleBackgroundGradientStartTaskPane = secondBackground;
        this.titleBackgroundGradientEndTaskPane = secondBackground;
        this.titleOverTaskPane = selectedForeground;
        this.specialTitleOverTaskPane = selectedForeground;
        this.backgroundTaskPane = this.backgroundPrimary;
        this.borderColorTaskPane = new ColorUIResource(211, 225, 232);
        this.contentBackgroundTaskPane = secondBackground;

        this.selectionBackgroundList = selectedBackground;
        this.selectionForegroundList = selectedForeground;

        this.backgroundProgressBar = disableBackground;
        this.foregroundProgressBar = this.highlightBackgroundPrimary;

        this.withoutIconSelectedBackgroundToggleButton = MaterialColors.COSMO_DARK_GRAY;
        this.withoutIconSelectedForegoundToggleButton = MaterialColors.BLACK;
        this.withoutIconBackgroundToggleButton = MaterialColors.GRAY_300;
        this.withoutIconForegroundToggleButton = MaterialColors.BLACK;

        this.colorDividierSplitPane = MaterialColors.COSMO_DARK_GRAY;
        this.colorDividierFocusSplitPane = selectedBackground;

        super.backgroundSeparator = MaterialColors.GRAY_300;
        super.foregroundSeparator = MaterialColors.GRAY_300;
    }

    @Override
    public void installUIDefault(UIDefaults table) {
        super.installUIDefault(table);
    }

    @Override
    public String getName() {
        return "Material Lite";
    }

    @Override
    public boolean getButtonBorderEnableToAll() {
        return true;
    }
}