/*
 * Copyright 2017-2018 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.akarnokd.rxjava3.swing;

import javax.swing.JComponent;
import javax.swing.event.*;

import io.reactivex.rxjava3.core.*;

final class AncestorEventObservable extends Observable<AncestorEvent> {

    final JComponent widget;

    AncestorEventObservable(JComponent widget) {
        this.widget = widget;
    }

    @Override
    protected void subscribeActual(Observer<? super AncestorEvent> observer) {
        JComponent w = widget;

        AncestorEventConsumer aec = new AncestorEventConsumer(observer, w);
        observer.onSubscribe(aec);

        w.addAncestorListener(aec);
        if (aec.get() == null) {
            aec.onDispose(w);
        }
    }

    static final class AncestorEventConsumer extends AbstractEventConsumer<AncestorEvent, JComponent>
    implements AncestorListener {

        private static final long serialVersionUID = -3605206827474016488L;

        AncestorEventConsumer(Observer<? super AncestorEvent> actual, JComponent widget) {
            super(actual, widget);
        }

        @Override
        protected void onDispose(JComponent w) {
            w.removeAncestorListener(this);
        }

        @Override
        public void ancestorAdded(AncestorEvent event) {
            actual.onNext(event);
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
            actual.onNext(event);
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
            actual.onNext(event);
        }

    }
}
