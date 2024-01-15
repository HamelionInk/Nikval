import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'ee121ed17b8c0db3eb959181e9802161cde81329fff508ee03f83daa6489d679') {
    pending.push(import('./chunks/chunk-f8482a38fa5ff407ea7ae821761db1fb68af8d3f19aa7b235d5bd40306f86aea.js'));
  }
  if (key === '430d1e6c3546416d2384b6ca76927e69e6b339d9dde3a754502a23c49324b437') {
    pending.push(import('./chunks/chunk-7c09407276d31623c51fc60e231d899cb431c994e18ce176684538f77dd629ef.js'));
  }
  if (key === '16d777ec677a64df91d500cfeca92cb140708490f2c3b85068ea4777dbacd37e') {
    pending.push(import('./chunks/chunk-e69226f22faf717520bebfb86879ce04d768edf3cd2ad2fd1ac88cb5b1c33305.js'));
  }
  if (key === '7690d23904d0d649e54c199a14e5e325bd5958e9d653052d894eff228e6ae6e7') {
    pending.push(import('./chunks/chunk-e69226f22faf717520bebfb86879ce04d768edf3cd2ad2fd1ac88cb5b1c33305.js'));
  }
  if (key === 'adcf943cb54e676541f259b4b3ca39bf0ee1402fc92f3359ab902c0e3c060296') {
    pending.push(import('./chunks/chunk-7c09407276d31623c51fc60e231d899cb431c994e18ce176684538f77dd629ef.js'));
  }
  if (key === 'fb0a7527cc32bf4e58142782dee7c01a702002a26b3c73b19f55ea8ab2ab09ca') {
    pending.push(import('./chunks/chunk-b1d55c3da6a33480b66500236374bf45e299bfe67f9c05c51f75370bfa2b9a5e.js'));
  }
  if (key === '650d140577cf394f018fb9f1c8604cc1104aa5331c210e05d0825e0d6d489bb1') {
    pending.push(import('./chunks/chunk-b1d55c3da6a33480b66500236374bf45e299bfe67f9c05c51f75370bfa2b9a5e.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;