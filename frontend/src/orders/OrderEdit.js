import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label, Table} from 'reactstrap';
import AppNavbar from '../AppNavbar';

class OrderEdit extends Component {

    emptyItem = {
        description: '',
        items: [],
        users: []
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const order = await (await fetch(`/orders/${this.props.match.params.id}`)).json();
            this.setState({item: order});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetch(('/orders/' + item.id + '/1'), {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/orders');
    }

    render() {
        const {item} = this.state;
        const title = <h2 style={{paddingTop: 50, paddingBottom: 50}}>Edit order</h2>;
        const titleTable1 = <h3 style={{paddingTop: 50}}>Items</h3>;
        const titleTable2 = <h3 style={{paddingTop: 50}}>Users</h3>;

        const itemList = item.items.map(i => {
            return <tr key={i.id}>
                <td style={{whiteSpace: 'nowrap'}}>{i.description}</td>
                <td>{i.quantity}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/orders/" + item.id + "/items/" + i.id}>Add</Button>
                        <Button size="sm" color="secondary" tag={Link} to={"/orders/" + item.id + "/items/" + i.id}>Remove</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(i.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        const userList = item.users.map(i => {
            return <tr key={i.id}>
                <td style={{whiteSpace: 'nowrap'}}>{i.username}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="danger" onClick={() => this.remove(i.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="text" name="description" id="description" defaultValue={item.description || ''}
                               onChange={this.handleChange} autoComplete="description"/>
                    </FormGroup>
                    {titleTable1}
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Quantity</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {itemList}
                        </tbody>
                    </Table>
                    {titleTable2}
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="60%">Name</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {userList}
                        </tbody>
                    </Table>
                    <FormGroup style={{paddingTop: 50}}>
                        <Button color="primary" type="submit" >Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/orders">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }

}
export default withRouter(OrderEdit);