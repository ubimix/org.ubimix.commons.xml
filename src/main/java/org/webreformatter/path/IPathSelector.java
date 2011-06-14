/**
 * 
 */
package org.webreformatter.path;

/**
 * @author kotelnikov
 */
public interface IPathSelector {

    INodeSelector getNodeSelector(int pos);

    int getSelectorNumber();

}