describe("Filter text", () => {
    it("filter text", () => {
        cy.visit("http://localhost:3000/");
        cy.get('input[placeholder="search by name"]')
            .type("Geller")
            .get('.row').then($row => {
                expect($row.length).to.eq(2)
            })
    });

});