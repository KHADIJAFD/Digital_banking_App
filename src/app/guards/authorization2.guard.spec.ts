import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { authorization2Guard } from './authorization2.guard';

describe('authorization2Guard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => authorization2Guard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
