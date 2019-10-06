//
//  TopicSearcherViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import DropDown

class TopicSearcherViewController: UIViewController {
    
    @IBOutlet weak var mSubmitAndPlayBtn: UIButton!
    
    @IBAction func mSubmitAndPlayClicked() {
        if mSubmitAndPlayBtn.state != .disabled {
            performSegue(withIdentifier: "launchRound1", sender: nil)
        }
    }
    
    
    @IBOutlet weak var mLocaleDropDown: DropDown!
    
    @IBOutlet weak var mTopicDropDown: DropDown!
    
    @IBOutlet weak var mSelectAnyHeader: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mSelectAnyHeader.text = NSLocalizedString("topic_searcher_heading", comment: "")
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "launchRound1":
            let destinationVC = segue.destination as! Round1ViewController
        default:
            break
        }
    }
    
}
