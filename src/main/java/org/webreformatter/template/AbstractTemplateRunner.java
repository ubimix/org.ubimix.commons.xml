/**
 * 
 */
package org.webreformatter.template;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author kotelnikov
 * @param <T>
 * @param <D>
 */
public abstract class AbstractTemplateRunner<T, D> {

    private class Context {

        private D fData;

        private Iterator<D> fDataIterator;

        private final T fNode;

        private Iterator<T> fTemplateIterator;

        private T fTemplateNode;

        public Context(T node, D data) {
            fNode = node;
            fData = data;
        }

        @SuppressWarnings("unchecked")
        public Context getNextChild() {
            if (fTemplateIterator == null) {
                fTemplateIterator = getChildren(fNode);
            }
            if (fTemplateIterator == null)
                return null;
            Context child = null;
            while (child == null) {
                if (fTemplateNode == null) {
                    fTemplateNode = fTemplateIterator.hasNext()
                        ? fTemplateIterator.next()
                        : null;
                    if (fTemplateNode == null)
                        break;
                }
                if (fDataIterator == null) {
                    fDataIterator = selectData(fTemplateNode, fData);
                    if (fDataIterator == null) {
                        fDataIterator = Arrays.asList(fData).iterator();
                    }
                }
                if (fDataIterator.hasNext()) {
                    D childData = fDataIterator.next();
                    child = new Context(fTemplateNode, childData);
                } else {
                    fTemplateNode = null;
                    fDataIterator = null;
                }
            }
            return child;
        }

    }

    public interface ITemplateListener<T, D> {

        boolean beginNode(T template, D data);

        void endNode(T template, D data);
    }

    public AbstractTemplateRunner() {
    }

    protected abstract Iterator<T> getChildren(T node);

    public void run(T template, D data, final ITemplateListener<T, D> listener) {
        Context topNode = new Context(template, data);
        visit(topNode, listener);
    }

    protected abstract Iterator<D> selectData(T node, D data);

    private void visit(Context node, ITemplateListener<T, D> listener) {
        if (listener.beginNode(node.fNode, node.fData)) {
            Context child;
            while ((child = node.getNextChild()) != null) {
                visit(child, listener);
            }
        }
        listener.endNode(node.fNode, node.fData);
    }
}