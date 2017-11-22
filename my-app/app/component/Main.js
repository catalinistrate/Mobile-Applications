import React from 'react';
import {StyleSheet, Text, View, TextInput, ScrollView, TouchableOpacity} from 'react-native';

import Comment from './Comment'
export default class Main extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            commentArray: [],
            commentText: '',
        }
    }

    render() {
        let comments  =  this.state.commentArray.map((val, key) => {
            return <Comment key={key} keyval={key} val={val}
                            deleteMethod={() => this.deleteComment(key)} />
        });

        return (
            <View style={styles.container}>
                <View style={styles.header}>
                    <Text style={styles.headerText}>FORUM</Text>
                </View>

                <ScrollView style={styles.scrollContainer}>
                    {comments}
                </ScrollView>


                <View style={styles.footer}>
                    <TextInput
                        style={styles.textInput}
                        onChangeText={(commentText) => this.setState({commentText})}
                        placeholder='>comment'
                        placeholderTextColor='white'
                        value={this.state.commentText}>
                    </TextInput>
                    <TouchableOpacity style={styles.addButton} onPress={this.addComment.bind(this)}>
                        <Text style={styles.addButtonText}>+</Text>
                    </TouchableOpacity>
                </View>
            </View>
        );
    }

    addComment() {
        if(this.state.commentText){
            var d = new Date();
            this.state.commentArray.push({
                'date': d.getFullYear() + '/' + (d.getMonth() + 1) + '/' + d.getDate(),
                'comment': this.state.commentText
            });
            this.setState({ commentArray: this.state.commentArray });
            this.setState({ commentText: ''});
        }
    }
    deleteComment(key){
        this.state.commentArray.splice(key,1);
        this.setState( { commentArray: this.state.commentArray } );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1
    },
    header: {
        backgroundColor: '#E91E63',
        alignItems: 'center',
        borderBottomWidth: 10,
        borderBottomColor: '#ddd'
    },
    headerText: {
        color: 'white',
        fontSize: 18,
        padding: 26
    },
    scrollContainer:
        {
            flex: 1,
            marginBottom: 100,
        },
    footer:
        {
            position: 'absolute',
            bottom: 0,
            left: 0,
            right: 0,
            zIndex: 10
        },
    textInput:
        {
            alignSelf: 'stretch',
            color: '#fff',
            padding: 20,
            backgroundColor: '#252525',
            borderTopWidth: 2,
            borderTopColor: '#ededed'
        },
    addButton:
        {
            position: 'absolute'
            , zIndex: 11,
            right: 20,
            bottom: 20,
            backgroundColor: '#E91E63',
            width: 90,
            height: 90,
            borderRadius: 50,
            alignItems: 'center',
            justifyContent: 'center',
            elevation: 8,
        },
    addButtonText:
        {
            color: '#ffffff',
            fontSize: 24,
        }
});
