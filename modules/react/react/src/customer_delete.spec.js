describe("Delete Customer", () => {
    it("delete customer", () => {
        cy.visit("http://localhost:3000/");
        
        cy.get('button').as('deletebutton');

        cy.get('@deletebutton').first().click().get('.row').then($row => {
            expect($row.length).to.eq(5)
        })
    });

});