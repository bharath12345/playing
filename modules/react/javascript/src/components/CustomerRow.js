import React, {Component} from 'react'

export default class CustomerRow extends Component {
    deleteRow(id) {
        console.log("row ", this.props, id);
        this.props.delEvent(id);
    }

    render() {
        let {customer} = this.props;
        return (
            <div className="row">
                {customer.firstName} &nbsp;
                {customer.lastName}
                <button type="button" onClick={() => this.deleteRow(customer.id)}>Delete</button>
            </div>
        )
    }
}

//<!--button type="button" onClick={this.deleteRow.bind(this)}>Delete</button-->

