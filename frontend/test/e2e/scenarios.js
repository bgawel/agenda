describe('Agenda App', function() {

  describe('Main view', function() {

    beforeEach(function() {
      browser().navigateTo('/');
    });

    it('sanity test to check if e2e testing works', function() {
      expect(browser().location().path()).toBe('/');
    }); 
  });
});