import time
import json

import plotly.graph_objects as go
import dash
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output

app = dash.Dash()
app.layout = html.Div([
        dcc.Graph(id = "live-update-graph"),
        dcc.Interval(
            id = "interval-component",
            interval=1*1000,
            n_intervals = 0
        )
    ])

@app.callback(Output('live-update-graph', 'figure'),
            Input('interval-component', 'n_intervals'))
              
def plot_donut_chart(n):
    
    with open("results_visualize.json", 'r') as openfile:
        json_object = json.load(openfile)
    
    num_pos = json_object["num_pos_tweet"]
    num_neg = json_object["num_neg_tweet"]
    num_neu = json_object["num_neu_tweet"]
    total_count = num_pos + num_neg + num_neu  # Calculate the total count of tweets

    labels = ['Positive', 'Negative', 'Neutral']
    values = [num_pos, num_neg, num_neu]
    fig = go.Figure(data = [go.Pie(labels = labels, values = values, hole = 0.5)])

    fig.data[0].update( { 'values': [num_pos, num_neg, num_neu]} )

    fig.update_traces(textposition = 'inside', 
                    textinfo = 'percent + label',
                    marker = dict(colors = ['#94dfff', '#ff6961', '#f0efeb'],
                                    line = dict(color = 'white', width = 2)))

    fig.update_layout(title = 'Sentiment Analysis',
                    annotations = [dict(text = 'Number of tweets:\n' + str(total_count), x = 0.5, y = 0.5, font_size = 20, showarrow = False)],
                    showlegend = False)
    
    return fig
    
if __name__ == "__main__":
    
    app.run_server()